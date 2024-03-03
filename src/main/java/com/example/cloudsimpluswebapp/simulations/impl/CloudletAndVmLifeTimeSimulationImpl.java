package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.dto.DatacenterDTO;
import com.example.cloudsimpluswebapp.dto.HostDTO;
import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.simulations.CloudletAndVmLifeTimeSimulation;
import org.cloudsimplus.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.builders.tables.MarkdownTableColumn;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Lifetimed;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.util.TimeUtil;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.utilizationmodels.UtilizationModelStochastic;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CloudletAndVmLifeTimeSimulationImpl implements CloudletAndVmLifeTimeSimulation {

    @Override
    public List<Cloudlet> startLifeTimeSimulation(SimulationDTO simulationDTO) {

        List<HostDTO> hosts = simulationDTO.getHostDTOS();
        List<CloudletDTO> cloudlets = simulationDTO.getCloudletDTOS();
        List<DatacenterDTO> datacenterDTOS = simulationDTO.getDatacenterDTOS();

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

        final double startSecs = TimeUtil.currentTimeSecs();

        System.out.printf("Simulation started at %s%n%n", LocalTime.now());

        cloudSimPlus.start();

        final var cloudletFinishedList = broker0.getCloudletFinishedList();
        cloudletFinishedList.sort(Comparator.comparingLong(c -> c.getVm().getId()));
        new CloudletsTableBuilder(cloudletFinishedList)
                .addColumn(new MarkdownTableColumn("Cloudlet", "LifeTime"), this::getLifeTimeStr)
                .addColumn(new MarkdownTableColumn("Vm      ", "LifeTime"), c -> getLifeTimeStr(c.getVm()))
                .build();
        System.out.printf("Simulation finished at %s. Execution time: %.2f seconds%n", LocalTime.now(), TimeUtil.elapsedSeconds(startSecs));

        return broker0.getCloudletFinishedList();
    }


    private String getLifeTimeStr(final Lifetimed entity) {
        return entity.getLifeTime() == Double.MAX_VALUE ? "" : "%.2f".formatted(entity.getLifeTime());
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
        return new HostSimple(host.getHostRam(), host.getHostBw(), host.getHostStorage(), peList).setVmScheduler(new VmSchedulerTimeShared());
    }

    private List<Cloudlet> createCloudlets(List<CloudletDTO> cloudlets) {
        final var cloudletList = new ArrayList<Cloudlet>();

        cloudlets.stream()
                .flatMap(cloudlet -> IntStream.range(0, cloudlet.getCloudletCount()).mapToObj(i->{
                    Cloudlet createdCloudlet = new CloudletSimple(cloudlet.getCloudletLength(), cloudlet.getCloudletPes());
                    createdCloudlet.setSizes(cloudlet.getCloudletSize());
                    createdCloudlet.setUtilizationModelCpu(new UtilizationModelFull());
                    createdCloudlet.setUtilizationModelBw(new UtilizationModelStochastic());
                    createdCloudlet.setUtilizationModelRam(new UtilizationModelStochastic());
                    createdCloudlet.setLifeTime(cloudlet.getCloudletLifetime());
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
                                            createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage()).setLifeTime(vm.getVmLifetime());
                                            return createdVm;
                                        }))
                        )
                )
                .forEach(vmStream -> vmStream.forEach(vmList::add));
        return vmList;
    }
}
