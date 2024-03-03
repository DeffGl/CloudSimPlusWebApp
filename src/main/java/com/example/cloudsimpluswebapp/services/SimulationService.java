package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.utils.exceptions.SimulationException;

public interface SimulationService {
    SimulationDTO startBasicSimulation(SimulationDTO simulationDTO) throws SimulationException;

    SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO) throws SimulationException;
}
