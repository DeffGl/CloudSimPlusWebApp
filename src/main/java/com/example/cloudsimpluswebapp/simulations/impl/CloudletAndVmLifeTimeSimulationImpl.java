package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.simulations.CloudletAndVmLifeTimeSimulation;
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

    int HOSTS = 3;
    int HOST_PES = 10;

    int VMS = 4;
    int VM_PES = 4;
    int VM_MIPS = 1000;

    int CLOUDLETS = 4;
    int CLOUDLET_PES = 2;
    int CLOUDLET_LENGTH = 10_000;

    /**
     * If the scheduling interval is not multiple of the VM/Cloudlet lifetime,
     * Cloudlets may execute more than you desire.
     * @see Datacenter#getSchedulingInterval()
     */
    int SCHEDULING_INTERVAL = 3;

    /**
     * Maximum time (in seconds) Cloudlets are allowed to execute.
     * Set -1 to disable lifeTime and execute the Cloudlet entirely.
     */
    double CLOUDLET_LIFE_TIME = 5;

    /**
     * Maximum time (in seconds) some VMs are allowed to execute.
     * @see Lifetimed#setLifeTime(double)
     */
    double VM_LIFE_TIME = 3;

    @Override
    public List<Cloudlet> startLifeTimeSimulation(SimulationDTO simulationDTO) {

        CloudSimPlus simulation;
        DatacenterBroker broker0;
        List<Vm> vmList;
        List<Cloudlet> cloudletList;
        Datacenter datacenter0;

         /*Enables just some level of log messages.
          Make sure to import org.cloudsimplus.util.Log;*/
        //Log.setLevel(ch.qos.logback.classic.Level.WARN);

        final double startSecs = TimeUtil.currentTimeSecs();
        System.out.printf("Simulation started at %s%n%n", LocalTime.now());
        simulation = new CloudSimPlus();
        datacenter0 = createDatacenter(simulation);

        //Creates a broker that is a software acting on behalf of a cloud customer to manage his/her VMs and Cloudlets
        broker0 = new DatacenterBrokerSimple(simulation);

        vmList = createVms();
        setVmsLifeTime(vmList);

        cloudletList = createCloudlets();
        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);

        simulation.start();

        final var cloudletFinishedList = broker0.getCloudletFinishedList();
        cloudletFinishedList.sort(Comparator.comparingLong(c -> c.getVm().getId()));
        new CloudletsTableBuilder(cloudletFinishedList)
                .addColumn(new MarkdownTableColumn("Cloudlet", "LifeTime"), this::getLifeTimeStr)
                .addColumn(new MarkdownTableColumn("Vm      ", "LifeTime"), c -> getLifeTimeStr(c.getVm()))
                .build();
        System.out.printf("Simulation finished at %s. Execution time: %.2f seconds%n", LocalTime.now(), TimeUtil.elapsedSeconds(startSecs));

        return null;
    }

    /**
     * Gets the lifetime as a String.
     * If the lifetime is {@link Double#MAX_VALUE} (the default value),
     * returns an empty string to indicate the attribute was not set.
     *
     * @param entity a Cloudlet of VM entity
     * @return a String lifetime
     */
    private String getLifeTimeStr(final Lifetimed entity) {
        return entity.getLifeTime() == Double.MAX_VALUE ? "" : "%.2f".formatted(entity.getLifeTime());
    }

    /**
     * Sets a lifetime for half of the VMs.
     */
    private void setVmsLifeTime(List<Vm> vmList) {
        vmList.stream()
                .filter(vm -> vm.getId() < VMS/2)
                .forEach(vm -> vm.setLifeTime(VM_LIFE_TIME));
    }

    private Datacenter createDatacenter(CloudSimPlus simulation) {
        final var hostList = new ArrayList<Host>(HOSTS);
        for(int i = 0; i < HOSTS; i++) {
            final var host = createHost();
            hostList.add(host);
        }

        final var datacenter = new DatacenterSimple(simulation, hostList);
        datacenter.setSchedulingInterval(SCHEDULING_INTERVAL);
        return datacenter;
    }

    private Host createHost() {
        final var peList = new ArrayList<Pe>(HOST_PES);
        //List of Host's CPUs (Processing Elements, PEs)
        IntStream.range(0, HOST_PES).forEach(i -> peList.add(new PeSimple(1000)));

        final long ram = 2048; //in Megabytes
        final long bw = 10000; //in Megabits/s
        final long storage = 1000000; //in Megabytes
        final var host = new HostSimple(ram, bw, storage, peList);
        host.setVmScheduler(new VmSchedulerTimeShared());
        return host;
    }


    private List<Cloudlet> createCloudlets() {
        final var cloudletList = new ArrayList<Cloudlet>(CLOUDLETS);
        for (int i = 0; i < CLOUDLETS; i++) {
            final var cloudlet = new CloudletSimple(i, CLOUDLET_LENGTH, CLOUDLET_PES);
            cloudlet
                    .setFileSize(1024)
                    .setOutputSize(1024)
                    .setUtilizationModelCpu(new UtilizationModelFull())
                    .setUtilizationModelBw(new UtilizationModelStochastic())
                    .setUtilizationModelRam(new UtilizationModelStochastic())
                    .setLifeTime(CLOUDLET_LIFE_TIME);
            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }

    private List<Vm> createVms() {
        final var vmList = new ArrayList<Vm>(VMS);
        for (int i = 0; i < VMS; i++) {
            final var vm = new VmSimple(i, VM_MIPS, VM_PES);
            vmList.add(vm);
        }

        return vmList;
    }
}
