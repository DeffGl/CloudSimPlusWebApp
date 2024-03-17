package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.dto.DatacenterDTO;
import com.example.cloudsimpluswebapp.dto.HostDTO;
import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.simulations.CloudletCancellationSimulation;
import org.cloudsimplus.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.core.Lifetimed;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.listeners.CloudletVmEventInfo;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class CloudletCancellationSimulationImpl implements CloudletCancellationSimulation {

    @Override
    public List<Cloudlet> startCloudletCancellationSimulation(SimulationDTO simulationDTO) {

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

        //Creates a broker that is a software acting on behalf of a cloud customer to manage his/her VMs and Cloudlets
        DatacenterBroker broker0 = new DatacenterBrokerSimple(cloudSimPlus);

        /**
         * Monitor the execution of the first Cloudlet to cancel it after it has executed 50%
         * of its total length.
         * In order to make this work, the {@link Datacenter#getSchedulingInterval()}
         * most be set.
         * See {@link #SCHEDULING_INTERVAL} for more details.
         */

        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);

        cloudSimPlus.start();

        final var cloudletFinishedList = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(cloudletFinishedList).build();

        return broker0.getCloudletFinishedList();
    }

    /**
     * Cancel a Cloudlet if it has already executed 50% of its total MIPS length.
     * @param e the event information about Cloudlet processing
     */
    public void cancelCloudletIfHalfExecuted(final CloudletVmEventInfo e, double maxExecutionTime) {

        final double currentTime = e.getTime();
        final Cloudlet cloudlet = e.getCloudlet();

        // Получаем время начала выполнения задачи
        final double startTime = cloudlet.getStartTime();

        // Вычисляем время выполнения задачи
        final double executionTime = currentTime - startTime;

        // Проверяем, превысила ли задача максимальное время выполнения
        if (executionTime >= maxExecutionTime) {
            System.out.printf(
                    "%n# %.2f: Cancelling %s execution as it has exceeded the maximum execution time.%n",
                    currentTime, cloudlet);
            cloudlet.getVm().getCloudletScheduler().cloudletCancel(cloudlet);
        }

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
        return new HostSimple(host.getHostRam(), host.getHostBw(), host.getHostStorage(), peList).setVmScheduler(new VmSchedulerTimeShared()).setRamProvisioner(new ResourceProvisionerSimple()).setBwProvisioner(new ResourceProvisionerSimple());
    }

    private List<Cloudlet> createCloudlets(List<CloudletDTO> cloudlets) {
        final var cloudletList = new ArrayList<Cloudlet>();

        cloudlets.stream()
                .flatMap(cloudlet -> IntStream.range(0, cloudlet.getCloudletCount()).mapToObj(i->{
                    Cloudlet createdCloudlet = new CloudletSimple(cloudlet.getCloudletLength(), cloudlet.getCloudletPes());
                    createdCloudlet.setSizes(cloudlet.getCloudletSize());
                    createdCloudlet.setUtilizationModelCpu(new UtilizationModelFull());
                    createdCloudlet.setUtilizationModelBw(new UtilizationModelDynamic(0.1));
                    createdCloudlet.setUtilizationModelRam(new UtilizationModelDynamic(0.2));
                    createdCloudlet.addOnUpdateProcessingListener(cloudletVmEventInfo -> cancelCloudletIfHalfExecuted(cloudletVmEventInfo, cloudlet.getCloudletMaxExecutionTime()));
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
                                            createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage());
                                            createdVm.setCloudletScheduler(new CloudletSchedulerTimeShared());
                                            return createdVm;
                                        }))
                        )
                )
                .forEach(vmStream -> vmStream.forEach(vmList::add));
        return vmList;
    }
}
