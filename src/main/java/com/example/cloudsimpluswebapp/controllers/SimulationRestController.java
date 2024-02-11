package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationRestController {
    private static final Logger log = LoggerFactory.getLogger(SimulationRestController.class);
    @PostMapping("/start")
    public ResponseEntity<String> startSimulation(@RequestBody SimulationDTO simulationDTO) {
        //TODO доделать базовую симуляцию
        log.info("TEST CHECK: " + simulationDTO);
        return ResponseEntity.ok("");
    }
}
