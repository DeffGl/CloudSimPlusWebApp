package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/simulation")
public class SimulationController {
    private static final Logger log = LoggerFactory.getLogger(SimulationController.class);
    private final ObjectMapper objectMapper;
    private final SimulationService simulationService;

    @Autowired
    public SimulationController(ObjectMapper objectMapper, SimulationService simulationService) {
        this.objectMapper = objectMapper;
        this.simulationService = simulationService;
    }

    @GetMapping
    public String getMainPage() {
        return "simulation/main";
    }

    @GetMapping("/basic")
    public String getBasicSimulationPage(Model model, @RequestParam(name = "simulationId", required = false) UUID simulationId) throws JsonProcessingException {
        SimulationDTO simulationDTO = null;
        if (simulationId !=null){
            simulationDTO = simulationService.getSimulation(simulationId);
        }
        log.info("TEST CHECK FIRTS" + simulationDTO);

        if (simulationDTO == null){
            simulationDTO = new SimulationDTO().setSimulationType(SimulationType.BASIC_SIMULATION);
        }
        log.info("TEST CHECK" + simulationDTO);
        model.addAttribute("simulationDTOJson", objectMapper.writeValueAsString(simulationDTO));
        model.addAttribute("simulationDTO", simulationDTO);
        return "simulation/basic";
    }

    @GetMapping("/lifetime")
    public String getCloudletAndVmLifeTimeSimulation(Model model, @RequestParam(name = "simulationId", required = false) UUID simulationId) throws JsonProcessingException {
        SimulationDTO simulationDTO = null;
        if (simulationId !=null){
            simulationDTO = simulationService.getSimulation(simulationId);
        }
        log.info("TEST CHECK FIRTS" + simulationDTO);

        if (simulationDTO == null){
            simulationDTO = new SimulationDTO().setSimulationType(SimulationType.LIFETIME_SIMULATION);
        }
        model.addAttribute("simulationDTOJson", objectMapper.writeValueAsString(simulationDTO));
        model.addAttribute("simulationDTO", simulationDTO);
        return "simulation/lifeTime";
    }
}
