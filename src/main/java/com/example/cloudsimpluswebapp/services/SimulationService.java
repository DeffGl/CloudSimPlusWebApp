package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;

public interface SimulationService {
    SimulationDTO startBasicSimulation(SimulationDTO simulationDTO);

    SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO);
}
