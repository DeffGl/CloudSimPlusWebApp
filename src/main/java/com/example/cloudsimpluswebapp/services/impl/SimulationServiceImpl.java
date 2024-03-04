package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.repositories.SimulationRepository;
import com.example.cloudsimpluswebapp.security.CurrentPersonResolver;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.simulations.BasicSimulation;
import com.example.cloudsimpluswebapp.simulations.CloudletAndVmLifeTimeSimulation;
import com.example.cloudsimpluswebapp.utils.exceptions.SimulationException;
import com.example.cloudsimpluswebapp.utils.mappers.SimulationMapper;
import com.example.cloudsimpluswebapp.utils.mappers.SimulationResultMapper;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationServiceImpl implements SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationServiceImpl.class);
    private final SimulationRepository simulationRepository;
    private final BasicSimulation basicSimulation;
    private final CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation;
    private final SimulationResultMapper simulationResultMapper;
    private final SimulationMapper simulationMapper;
    private final CurrentPersonResolver currentPersonResolver;

    @Autowired
    public SimulationServiceImpl(SimulationRepository simulationRepository, BasicSimulation basicSimulation, SimulationResultMapper simulationResultMapper, SimulationMapper simulationMapper, CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation, CurrentPersonResolver currentPersonResolver) {
        this.simulationRepository = simulationRepository;
        this.basicSimulation = basicSimulation;
        this.simulationResultMapper = simulationResultMapper;
        this.simulationMapper = simulationMapper;
        this.cloudletAndVmLifeTimeSimulation = cloudletAndVmLifeTimeSimulation;
        this.currentPersonResolver = currentPersonResolver;
    }

    @Override
    public SimulationDTO startBasicSimulation(SimulationDTO simulationDTO) throws SimulationException {
        try {
            List<Cloudlet> resultList = basicSimulation.startBasicSimulation(simulationDTO);
            simulationDTO.setSimulationResultDTOS(resultList.stream().map(simulationResultMapper::map).toList());
            simulationRepository.save(simulationMapper.map(simulationDTO).setPerson(currentPersonResolver.getCurrentPerson()));
        } catch (Exception e){
            throwException(e, simulationDTO);
        }
        return simulationDTO;
    }

    @Override
    public SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO) throws SimulationException {
        try {
            List<Cloudlet> resultList = cloudletAndVmLifeTimeSimulation.startLifeTimeSimulation(simulationDTO);
            simulationDTO.setSimulationResultDTOS(resultList.stream().map(simulationResultMapper::map).toList());
            simulationRepository.save(simulationMapper.map(simulationDTO).setPerson(currentPersonResolver.getCurrentPerson()));
        } catch (Exception e){
            throwException(e, simulationDTO);
        }
        return simulationDTO;
    }

    @Override
    public List<SimulationDTO> getSimulationsByPerson() {
        return simulationRepository.getSimulationsByPerson(currentPersonResolver.getCurrentPerson()).stream().map(simulationMapper::map).toList();
    }

    private void throwException(Exception e, SimulationDTO simulationDTO) throws SimulationException {
        //TODO Подумать над обработкой ошибок
        log.error(String.format("Произошла ошибка при запуске симуляции с именем: %s", simulationDTO.getNameSimulation()));
        throw new SimulationException(e);
    }
}
