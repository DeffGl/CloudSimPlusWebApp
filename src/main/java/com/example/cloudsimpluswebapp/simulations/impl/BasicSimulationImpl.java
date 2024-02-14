package com.example.cloudsimpluswebapp.simulations.impl;

import com.example.cloudsimpluswebapp.models.Simulation;
import com.example.cloudsimpluswebapp.services.impl.SimulationServiceImpl;
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

@Service
public class BasicSimulationImpl implements BasicSimulation {
    private static final Logger log = LoggerFactory.getLogger(BasicSimulationImpl.class);
    @Override
    public List<Cloudlet> startSimulation(Simulation simulation) {

        log.info("TEST CHECK DOSHEl SIMULATION" + simulation);
        List<com.example.cloudsimpluswebapp.models.Host> hosts = simulation.getHosts();
        List<com.example.cloudsimpluswebapp.models.Cloudlet> cloudlets = simulation.getCloudlets();

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
        List<Cloudlet> cloudletFinishedList = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(cloudletFinishedList).build();
        log.info("DOSHEL DO KONCA");
        return cloudletFinishedList;
    }

    private Datacenter createDatacenter(List<com.example.cloudsimpluswebapp.models.Host> hosts, CloudSimPlus cloudSimPlus) {
        List<Host>hostList = new ArrayList<>(hosts.size());
        for(com.example.cloudsimpluswebapp.models.Host host : hosts) {
            Host createdHost = createHost(host);
            hostList.add(createdHost);
        }

        //Uses a VmAllocationPolicySimple by default to allocate VMs
        return new DatacenterSimple(cloudSimPlus, hostList);
    }

    private Host createHost(com.example.cloudsimpluswebapp.models.Host host) {
        List<Pe> peList = new ArrayList<>(host.getHostPes());
        for (int i = 0; i < host.getHostPes(); i++) {
            peList.add(new PeSimple(host.getHostMips()));
        }
        return new HostSimple(host.getHostRam(), host.getHostBw(), host.getHostStorage(), peList);
    }

    /**
     * Creates a list of VMs.
     */
   private List<Vm> createVms(List<com.example.cloudsimpluswebapp.models.Host> hosts) {
       List<Vm> vmList = new ArrayList<>();
       for (com.example.cloudsimpluswebapp.models.Host host : hosts){
           List<com.example.cloudsimpluswebapp.models.Vm> vms = host.getVms();

           for (com.example.cloudsimpluswebapp.models.Vm vm : vms){
               Vm createdVm = new VmSimple(host.getHostMips(), vm.getVmPes());
               createdVm.setRam(vm.getVmRam()).setBw(vm.getVmBw()).setSize(vm.getVmStorage());
               vmList.add(createdVm);
           }
       }

        return vmList;
    }

    /**
     * Creates a list of Cloudlets.
     */
    private List<Cloudlet> createCloudlets(List<com.example.cloudsimpluswebapp.models.Cloudlet> cloudlets) {
        final var cloudletList = new ArrayList<Cloudlet>(cloudlets.size());

        //UtilizationModel defining the Cloudlets use only 50% of any resource all the time
        final var utilizationModel = new UtilizationModelDynamic(0.5);

        for (com.example.cloudsimpluswebapp.models.Cloudlet cloudlet : cloudlets) {
            Cloudlet createdCloudlet = new CloudletSimple(cloudlet.getCloudletLength(), cloudlet.getCloudletPes(), utilizationModel);
            createdCloudlet.setSizes(cloudlet.getCloudletSize());
            cloudletList.add(createdCloudlet);
        }

        return cloudletList;
    }
}
