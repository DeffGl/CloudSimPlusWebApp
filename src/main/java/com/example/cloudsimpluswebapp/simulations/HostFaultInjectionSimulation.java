package com.example.cloudsimpluswebapp.simulations;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import org.cloudsimplus.cloudlets.Cloudlet;

import java.util.List;

public interface HostFaultInjectionSimulation {
    List<Cloudlet> startHostFaultInjectionSimulation(SimulationDTO simulationDTO);
}
