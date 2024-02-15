package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.utils.mappers.SimulationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/simulation")
public class SimulationRestController {
    private static final Logger log = LoggerFactory.getLogger(SimulationRestController.class);

    private final SimulationService simulationService;
    private final SimulationMapper simulationMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public SimulationRestController(SimulationService simulationService, SimulationMapper simulationMapper, ObjectMapper objectMapper) {
        this.simulationService = simulationService;
        this.simulationMapper = simulationMapper;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestBody SimulationDTO simulationDTO) {
        String simulationListJson = "";
        log.info("TEST CHECK: " + simulationDTO);
        try {
            List<Cloudlet> cloudletList = simulationService.simulationStart(simulationMapper.map(simulationDTO));
            simulationListJson = objectMapper.writeValueAsString(simulationDTO);
            log.info("TEST CHECK: " + cloudletList);
            log.info("TEST CHECK: " + simulationListJson);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(simulationListJson);
    }
}
