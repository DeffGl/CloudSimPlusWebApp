package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.example.cloudsimpluswebapp.utils.exceptions.SimulationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<String> startBasicSimulation(@RequestBody SimulationDTO simulationDTO) throws JsonProcessingException, SimulationException {
        simulationDTO = simulationService.startBasicSimulation(simulationDTO);
        String simulationResultJson = objectMapper.writeValueAsString(simulationDTO);
        return ResponseEntity.ok(simulationResultJson);
    }

    @PostMapping("/lifetime")
    public ResponseEntity<String> startLifeTimeSimulation(@RequestBody SimulationDTO simulationDTO) throws JsonProcessingException, SimulationException {
        simulationDTO = simulationService.startLifeTimeSimulation(simulationDTO);
        String simulationResultJson = objectMapper.writeValueAsString(simulationDTO);
        return ResponseEntity.ok(simulationResultJson);

    }

    @PostMapping("/cancel")
    public ResponseEntity<String> startCloudletCancellationSimulation(@RequestBody SimulationDTO simulationDTO) throws SimulationException, JsonProcessingException {
        simulationDTO = simulationService.startCloudletCancellationSimulation(simulationDTO);
        log.info(simulationDTO.toString());
        String simulationResultJson = objectMapper.writeValueAsString(simulationDTO);
        return ResponseEntity.ok(simulationResultJson);
    }

    @GetMapping("/types")
    public ResponseEntity<String> getTypesSimulation() throws JsonProcessingException {
        Map<String, Object> simulationTypes = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        Arrays.stream(SimulationType.values()).forEach(simulationType -> simulationTypes.put(simulationType.getLocalizationName(), simulationType.name()));
        response.put("simulationTypes", simulationTypes);
        return ResponseEntity.ok(objectMapper.writeValueAsString(response));
    }
}
