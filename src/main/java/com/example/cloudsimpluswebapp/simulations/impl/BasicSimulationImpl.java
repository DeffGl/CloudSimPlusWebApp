package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.dto.HostDTO;
import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.simulations.BasicSimulation;
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
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class BasicSimulationImpl implements BasicSimulation {
    private static final Logger log = LoggerFactory.getLogger(BasicSimulationImpl.class);
    @Override
    public List<Cloudlet> startSimulation(SimulationDTO simulationDTO) {
        //TODO Привести в порядок метод симуляции
        List<HostDTO> hosts = simulationDTO.getHostDTOS();
        List<CloudletDTO> cloudlets = simulationDTO.getCloudletDTOS();

        CloudSimPlus cloudSimPlus;
        DatacenterBroker broker0;
        List<Vm> vmList;
        List<Cloudlet> cloudletList;
        Datacenter datacenter0;

        cloudSimPlus = new CloudSimPlus();
        datacenter0 = createDatacenter(hosts, cloudSimPlus);

        //Creates a broker that is a software acting on behalf of a cloud customer to manage his/her VMs and Cloudlets
        broker0 = new DatacenterBrokerSimple(cloudSimPlus);

        vmList = createVms(hosts);
        cloudletList = createCloudlets(cloudlets);
        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);

        cloudSimPlus.start();
        new CloudletsTableBuilder(broker0.getCloudletFinishedList()).build();
        broker0.getCloudletFinishedList().get(0);
        return broker0.getCloudletFinishedList();
    }

    private Datacenter createDatacenter(List<HostDTO> hosts, CloudSimPlus cloudSimPlus) {
        List<Host>hostList = new ArrayList<>();
        hosts.stream()
                .flatMap(host -> IntStream.range(0, host.getHostCount()).mapToObj(i -> createHost(host)))
                .forEach(hostList::add);

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        return new DatacenterSimple(cloudSimPlus, hostList);
    }

    private Host createHost(HostDTO host) {
        List<Pe> peList = new ArrayList<>();
        IntStream.range(0, host.getHostPes())
                .mapToObj(i -> new PeSimple(host.getHostMips()))
                .forEach(peList::add);
        return new HostSimple(host.getHostRam(), host.getHostBw(), host.getHostStorage(), peList);
    }

    /**
     * Creates a list of VMs.
     */
   private List<Vm> createVms(List<HostDTO> hosts) {
       List<Vm> vmList = new ArrayList<>();
       hosts.stream()
               .flatMap(host -> IntStream.range(0, host.getHostCount())
                       .mapToObj(i -> host.getVmDTOS().stream()
                               .flatMap(vm -> IntStream.range(0, vm.getVmCount())
                                       .mapToObj(j -> {
                                           Vm createdVm = new VmSimple(host.getHostMips(), vm.getVmPes());
                                           createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage());
                                           return createdVm;
                                       }))
                       )
               )
               .forEach(vmStream -> vmStream.forEach(vmList::add));
        return vmList;
    }

    /**
     * Creates a list of Cloudlets.
     */
    private List<Cloudlet> createCloudlets(List<CloudletDTO> cloudlets) {
        final var cloudletList = new ArrayList<Cloudlet>();
        //UtilizationModel defining the Cloudlets use only 50% of any resource all the time
        final var utilizationModel = new UtilizationModelDynamic(0.5);


        cloudlets.stream()
                .flatMap(cloudlet -> IntStream.range(0, cloudlet.getCloudletCount()).mapToObj(i->{
                    Cloudlet createdCloudlet = new CloudletSimple(cloudlet.getCloudletLength(), cloudlet.getCloudletPes(), utilizationModel);
                    createdCloudlet.setSizes(cloudlet.getCloudletSize());
                    return createdCloudlet;
                })).forEach(cloudletList::add);
        return cloudletList;
    }
}
