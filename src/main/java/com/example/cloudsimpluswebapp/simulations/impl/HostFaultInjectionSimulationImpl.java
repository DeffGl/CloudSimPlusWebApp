package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.*;
import com.example.cloudsimpluswebapp.simulations.HostFaultInjectionSimulation;
import org.cloudsimplus.allocationpolicies.VmAllocationPolicyBestFit;
import org.cloudsimplus.brokers.DatacenterBroker;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.distributions.PoissonDistr;
import org.cloudsimplus.faultinjection.HostFaultInjection;
import org.cloudsimplus.faultinjection.VmClonerSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.provisioners.ResourceProvisionerSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.util.TimeUtil;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.utilizationmodels.UtilizationModelFull;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
@Service
public class HostFaultInjectionSimulationImpl implements HostFaultInjectionSimulation {

    private static final Logger log = LoggerFactory.getLogger(HostFaultInjectionSimulationImpl.class);
    private static final int SCHEDULE_TIME_TO_PROCESS_DATACENTER_EVENTS = 0;
    private static final int BROKERS = 1;
    private static final int HOSTS = 8;
    private static final int HOST_PES = 5;
    private static final int HOST_MIPS_BY_PE = 1000;
    private static final long HOST_RAM = 500000; //host memory (Megabyte)
    private static final long HOST_STORAGE = 1000000; //host storage
    private static final long HOST_BW = 100000000L;
    private static final double MEAN_FAILURE_NUMBER_PER_HOUR = 0.01;
    private static final double MAX_TIME_TO_FAIL_IN_HOURS = TimeUtil.daysToHours(30);

    private static final int VMS_BY_BROKER = 2;
    private static final int VM_PES = 2; //number of cpus
    private static final int VM_MIPS = 1000;
    private static final long VM_SIZE = 1000; //image size (Megabyte)
    private static final int VM_RAM = 10000; //vm memory (Megabyte)
    private static final long VM_BW = 100000;
    private static final int CLOUDLETS_BY_BROKER = 6;
    private static final int CLOUDLET_PES = 2;
    private static final long CLOUDLET_LENGTH = 1_000_000_000L;
    private static final long CLOUDLET_FILESIZE = 300;
    private static final long CLOUDLET_OUTPUT_SIZE = 300;
    private CloudSimPlus simulation;
    private int createdVms;
    private int createdCloudlets;
    private HostFaultInjection fault;
    private PoissonDistr poisson;

    public HostFaultInjectionSimulationImpl() {

    }

    @Override
    public List<DatacenterBrokerSimple> startHostFaultInjectionSimulation(SimulationDTO simulationDTO) {

        List<HostDTO> hosts = simulationDTO.getHostDTOS();
        List<CloudletDTO> cloudlets = simulationDTO.getCloudletDTOS();
        List<DatacenterDTO> datacenterDTOS = simulationDTO.getDatacenterDTOS();
        List<DatacenterBrokerDTO> datacenterBrokerDTOS = simulationDTO.getDatacenterBrokerDTOS();

        CloudSimPlus cloudSimPlus = new CloudSimPlus();
        List<Host> hostList = createHosts(hosts);
        List<Vm> vmList = createVms(hosts);
        List<Cloudlet> cloudletList = createCloudlets(cloudlets);


        Datacenter datacenter0 = new DatacenterSimple(cloudSimPlus, hostList, new VmAllocationPolicyBestFit());

        //List<DatacenterBrokerSimple> brokerList = IntStream.range(0, datacenterBrokerDTOS.size()).mapToObj(i -> new DatacenterBrokerSimple(cloudSimPlus)).toList();
        List<DatacenterBrokerSimple> brokerList  = IntStream.range(0, BROKERS).mapToObj(i -> {
            DatacenterBrokerSimple broker0 = new DatacenterBrokerSimple(cloudSimPlus);
            broker0.submitVmList(vmList);
            broker0.submitCloudletList(cloudletList);
            return broker0;
        }).toList();
        System.out.println("Starting " + getClass().getSimpleName());
        DatacenterDTO datacenterDTO = new DatacenterDTO();
        if (!datacenterDTOS.isEmpty()) {
            datacenterDTO = datacenterDTOS.get(0);
            datacenter0.setSchedulingInterval(datacenterDTO.getSchedulingInterval());
            fault = createFaultInjectionForHosts(datacenter0, datacenterDTO, brokerList);
        }

        cloudSimPlus.start();
        printResults(brokerList, datacenterDTO, cloudSimPlus);

        System.out.println(getClass().getSimpleName() + " finished!");
        return brokerList;
    }

    private void printResults(List<DatacenterBrokerSimple> brokerList, DatacenterDTO datacenterDTO, CloudSimPlus cloudSimPlus) {
        brokerList.forEach(broker -> new CloudletsTableBuilder(broker.getCloudletFinishedList()).setTitle(broker.toString()).build());

        final int k = poisson.getK();
        final double interArrival = poisson.getInterArrivalMeanTime();

        System.out.printf("%n# Total simulation time: %s%n", TimeUtil.secondsToStr(cloudSimPlus.clock()));
        System.out.printf("# Number of Host faults: %d%n", fault.getHostFaultsNumber());
        System.out.printf(
                "# Mean Number of Failures per Hour: %.3f (%.3f x %.0f = %d failure expected at each %.0f hours).%n",
                datacenterDTO.getHostMeanFailureNumberPerHour(), datacenterDTO.getMaxTimeToFailInHours(), interArrival, k, interArrival);
        System.out.printf("# Number of faults affecting all VMs from a broker: %d%n", fault.getTotalFaultsNumber());
        System.out.printf("# Mean Time To Repair Failures of VMs (MTTR): %.2f minutes%n", fault.meanTimeToRepairVmFaultsInMinutes());
        System.out.printf("# Mean Time Between Failures (MTBF) affecting all VMs: %.2f minutes%n", fault.meanTimeBetweenVmFaultsInMinutes());
        System.out.printf("# Hosts MTBF: %.2f minutes%n", fault.meanTimeBetweenHostFaultsInMinutes());
        System.out.printf("# Availability: %.4f%%%n%n", fault.availability()*100);
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
                                            createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage());
                                            createdVm.setCloudletScheduler(new CloudletSchedulerTimeShared());
                                            return createdVm;
                                        }))
                        )
                )
                .forEach(vmStream -> vmStream.forEach(vmList::add));
        return vmList;
    }

    private HostFaultInjection createFaultInjectionForHosts(Datacenter datacenter, DatacenterDTO datacenterDTO, List<DatacenterBrokerSimple> brokerList) {
        final long seed = 112717613L;
        this.poisson = new PoissonDistr(datacenterDTO.getHostMeanFailureNumberPerHour(), seed);

        final var fault = new HostFaultInjection(datacenter, poisson);
        fault.setMaxTimeToFailInHours(datacenterDTO.getMaxTimeToFailInHours());

        for (final DatacenterBroker broker : brokerList) {
            fault.addVmCloner(broker, new VmClonerSimple(this::cloneVm, this::cloneCloudlets));
        }

        return fault;
    }

    private Vm cloneVm(final Vm vm) {
        final Vm clone = new VmSimple(vm.getMips(), vm.getPesNumber());
        clone.setId(vm.getId() * 10);
        clone.setDescription("Clone of Vm " + vm.getId());
        clone
                .setSize(vm.getStorage().getCapacity())
                .setBw(vm.getBw().getCapacity())
                .setRam(vm.getBw().getCapacity())
                .setCloudletScheduler(new CloudletSchedulerTimeShared());

        System.out.printf(
                "%n# %s: Cloning %s as Vm %d -> MIPS: %.0f PEs Number: %d%n",
                vm.getBroker(), vm, clone.getId(), clone.getMips(), clone.getPesNumber());

        return clone;
    }

    private List<Cloudlet> cloneCloudlets(final Vm sourceVm) {
        final var sourceVmCloudletList = sourceVm.getCloudletScheduler().getCloudletList();
        final var clonedCloudletList = new ArrayList<Cloudlet>(sourceVmCloudletList.size());
        for (final var sourceCloudlet : sourceVmCloudletList) {
            final var clonedCloudlet = cloneCloudlet(sourceCloudlet);
            clonedCloudletList.add(clonedCloudlet);
            System.out.printf(
                    "# %s: Cloning %s as Cloudlet %d%n",
                    sourceVm.getBroker(), sourceCloudlet, clonedCloudlet.getId(), sourceVm);
        }

        return clonedCloudletList;
    }

    private Cloudlet cloneCloudlet(Cloudlet source) {
        final var clone = new CloudletSimple(source.getLength(), source.getPesNumber());
        clone.setId(source.getId() * 10);
        clone
                .setUtilizationModelBw(source.getUtilizationModelBw())
                .setUtilizationModelCpu(source.getUtilizationModelCpu())
                .setUtilizationModelRam(source.getUtilizationModelRam());
        return clone;
    }
}
