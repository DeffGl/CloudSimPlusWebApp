package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.controllers.SimulationRestController;
import com.example.cloudsimpluswebapp.models.Simulation;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.simulations.BasicSimulation;
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

    @Autowired
    public SimulationServiceImpl(BasicSimulation basicSimulation) {
        this.basicSimulation = basicSimulation;
    }

    @Override
    public List<Cloudlet> simulationStart(Simulation simulation) {
        try {
            log.info("TEST CHECK DOSHEl SERVICE");
            return basicSimulation.startSimulation(simulation);
        }catch (Exception e){
            e.printStackTrace();
            log.info("TEST CHECK: SLOMALOS");
        }
        return null;
    }
}
