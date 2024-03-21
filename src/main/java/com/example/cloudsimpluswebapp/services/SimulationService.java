package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.utils.exceptions.SimulationException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface SimulationService {
    SimulationDTO startBasicSimulation(SimulationDTO simulationDTO) throws SimulationException;

    SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO) throws SimulationException;

    SimulationDTO startCloudletCancellationSimulation(SimulationDTO simulationDTO) throws SimulationException;

    List<SimulationDTO> getSimulationsByPerson();
    Page<SimulationDTO> getSimulationsByPerson(int page);

    SimulationDTO getSimulation(UUID simulationId);
}
