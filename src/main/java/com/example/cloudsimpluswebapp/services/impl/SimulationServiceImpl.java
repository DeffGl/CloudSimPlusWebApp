package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.simulations.BasicSimulation;
import com.example.cloudsimpluswebapp.simulations.CloudletAndVmLifeTimeSimulation;
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
    private final BasicSimulation basicSimulation;
    private final SimulationResultMapper simulationResultMapper;
    private final SimulationMapper simulationMapper;

    private final CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation;

    @Autowired
    public SimulationServiceImpl(BasicSimulation basicSimulation, SimulationResultMapper simulationResultMapper, SimulationMapper simulationMapper, CloudletAndVmLifeTimeSimulation cloudletAndVmLifeTimeSimulation) {
        this.basicSimulation = basicSimulation;
        this.simulationResultMapper = simulationResultMapper;
        this.simulationMapper = simulationMapper;
        this.cloudletAndVmLifeTimeSimulation = cloudletAndVmLifeTimeSimulation;
    }

    @Override
    public SimulationDTO startBasicSimulation(SimulationDTO simulationDTO) {
        try {
            //TODO Привести в порядок метод сервиса
            List<Cloudlet> resultList = basicSimulation.startBasicSimulation(simulationDTO);
            simulationDTO.setSimulationResultDTOS(resultList.stream().map(simulationResultMapper::map).toList());
            return simulationDTO;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SimulationDTO startLifeTimeSimulation(SimulationDTO simulationDTO) {
        try {
            List<Cloudlet> resultList = cloudletAndVmLifeTimeSimulation.startLifeTimeSimulation(simulationDTO);
            simulationDTO.setSimulationResultDTOS(resultList.stream().map(simulationResultMapper::map).toList());
            return simulationDTO;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
