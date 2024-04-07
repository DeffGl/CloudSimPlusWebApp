package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.*;
import com.example.cloudsimpluswebapp.simulations.VmBootTimeAndOverhead;
import org.cloudsimplus.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.listeners.VmHostEventInfo;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.utilizationmodels.BootModel;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class VmBootTimeAndOverheadImpl implements VmBootTimeAndOverhead {

    @Override
    public List<Cloudlet> startVmBootTimeAndOverhead(SimulationDTO simulationDTO) {

        List<HostDTO> hosts = simulationDTO.getHostDTOS();
        List<CloudletDTO> cloudlets = simulationDTO.getCloudletDTOS();
        List<DatacenterDTO> datacenterDTOS = simulationDTO.getDatacenterDTOS();
        List<DatacenterBrokerDTO> datacenterBrokerDTOS = simulationDTO.getDatacenterBrokerDTOS();

        CloudSimPlus cloudSimPlus = new CloudSimPlus();
        List<Host> hostList = createHosts(hosts);
        List<Vm> vmList = createVms(hosts);
        List<Cloudlet> cloudletList = createCloudlets(cloudlets);

        Datacenter datacenter0 = new DatacenterSimple(cloudSimPlus, hostList, new VmAllocationPolicyBestFit());

        if (!datacenterDTOS.isEmpty()){
            datacenter0.setSchedulingInterval(datacenterDTOS.get(0).getSchedulingInterval());
        }

        DatacenterBroker broker0 = new DatacenterBrokerSimple(cloudSimPlus);

        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);

        broker0.submitVmList(vmList);
        vmList.get(0).addOnUpdateProcessingListener(this::updateVmProcessingListener);
        broker0.submitCloudletList(cloudletList);

        cloudSimPlus.start();

        final var cloudletFinishedList = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(cloudletFinishedList).build();
        return cloudletFinishedList;
    }

    private void updateVmProcessingListener(final VmHostEventInfo info) {
        final var vm = info.getVm();
        final String status = vm.isStartingUp() ? "(booting)" : "(running)";
        System.out.printf("%6.2f: %s %s CPU %5.1f%% RAM %5.1f%%%n", info.getTime(), vm, status, getVmCpuUsage(vm), getVmRamUsage(vm));

    }

    private static double getVmCpuUsage(Vm vm) {
        return vm.getCpuPercentUtilization() * 100.0;
    }


    private static double getVmRamUsage(final Vm vm) {
        return vm.getRam().getPercentUtilization() * 100.0;
    }


    private List<Host> createHosts(List<HostDTO> hosts) {
        List<Host> hostList = new ArrayList<>();
        hosts.stream()
                .flatMap(host -> IntStream.range(0, host.getHostCount()).mapToObj(i -> createHost(host)))
                .forEach(hostList::add);
        return hostList;
    }

    private Host createHost(HostDTO host) {
        List<Pe> peList = new ArrayList<>();
        IntStream.range(0, host.getHostPes())
                .mapToObj(i -> new PeSimple(host.getHostMips()))
                .forEach(peList::add);
        return new HostSimple(host.getHostRam(), host.getHostBw(), host.getHostStorage(), peList).setVmScheduler(new VmSchedulerTimeShared()).setRamProvisioner(new ResourceProvisionerSimple()).setBwProvisioner(new ResourceProvisionerSimple());
    }

    private List<Cloudlet> createCloudlets(List<CloudletDTO> cloudlets) {
        final var cloudletList = new ArrayList<Cloudlet>();

        cloudlets.stream()
                .flatMap(cloudlet -> IntStream.range(0, cloudlet.getCloudletCount()).mapToObj(i -> {
                    Cloudlet createdCloudlet = new CloudletSimple(cloudlet.getCloudletLength(), cloudlet.getCloudletPes());
                    createdCloudlet.setSizes(cloudlet.getCloudletSize());
                    createdCloudlet.setUtilizationModelCpu(new UtilizationModelFull());
                    createdCloudlet.setUtilizationModelBw(new UtilizationModelDynamic(0.1));
                    createdCloudlet.setUtilizationModelRam(new UtilizationModelDynamic(0.2));
                    return createdCloudlet;
                })).forEach(cloudletList::add);
        return cloudletList;
    }

    private List<Vm> createVms(List<HostDTO> hosts) {
        List<Vm> vmList = new ArrayList<>();
        hosts.stream()
                .flatMap(host -> IntStream.range(0, host.getHostCount())
                        .mapToObj(i -> host.getVmDTOS().stream()
                                .flatMap(vm -> IntStream.range(0, vm.getVmCount())
                                        .mapToObj(j -> {
                                            Vm createdVm = new VmSimple(host.getHostMips(), vm.getVmPes());
                                            final var bootModelCpu = new UtilizationModelDynamic(0.5);
                                            final var bootModelRam = new UtilizationModelDynamic(0.3);
                                            createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage());
                                            createdVm.setCloudletScheduler(new CloudletSchedulerTimeShared());
                                            createdVm.setBootModel(new BootModel(bootModelCpu, bootModelRam))
                                                    .setStartupDelay(vm.getVmBootDelay())
                                                    .setShutDownDelay(vm.getVmShutdownDelay());
                                            return createdVm;
                                        }))
                        )
                )
                .forEach(vmStream -> vmStream.forEach(vmList::add));
        return vmList;
    }
}
