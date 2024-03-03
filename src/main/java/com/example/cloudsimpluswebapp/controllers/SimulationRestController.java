package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationRestController {
    private static final Logger log = LoggerFactory.getLogger(SimulationRestController.class);

    private final SimulationService simulationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SimulationRestController(SimulationService simulationService, ObjectMapper objectMapper) {
        this.simulationService = simulationService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/basic")
    public ResponseEntity<String> startBasicSimulation(@RequestBody SimulationDTO simulationDTO) {
        String simulationResultJson = "";
        try {
            log.info("TEST CHECK: " + simulationDTO);
            simulationDTO = simulationService.startBasicSimulation(simulationDTO);
            simulationResultJson = objectMapper.writeValueAsString(simulationDTO);
            log.info("TEST CHECK: " + simulationResultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(simulationResultJson);
    }

    @PostMapping("/lifetime")
    public ResponseEntity<String> startLifeTimeSimulation(@RequestBody SimulationDTO simulationDTO){
        String simulationResultJson = "";
        try {
            log.info("TEST CHECK: " + simulationDTO);
            simulationDTO = simulationService.startLifeTimeSimulation(simulationDTO);
            simulationResultJson = objectMapper.writeValueAsString(simulationDTO);
            log.info("TEST CHECK: " + simulationResultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(simulationResultJson);

    }
}
