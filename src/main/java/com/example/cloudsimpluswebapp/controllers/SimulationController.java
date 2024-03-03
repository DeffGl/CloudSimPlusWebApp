package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    private final ObjectMapper objectMapper;

    @Autowired
    public SimulationController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public String getMainPage() {
        return "simulation/main";
    }

    @GetMapping("/basic")
    public String getBasicSimulationPage(Model model) throws JsonProcessingException {
        SimulationDTO simulationDTO = new SimulationDTO().setSimulationType(SimulationType.BASIC_SIMULATION);
        model.addAttribute("simulationDTOJson", objectMapper.writeValueAsString(simulationDTO));
        model.addAttribute("simulationDTO", simulationDTO);
        return "simulation/basic";
    }

    @GetMapping("/lifetime")
    public String getCloudletAndVmLifeTimeSimulation(Model model) throws JsonProcessingException {
        SimulationDTO simulationDTO = new SimulationDTO().setSimulationType(SimulationType.LIFETIME_SIMULATION);
        model.addAttribute("simulationDTOJson", objectMapper.writeValueAsString(simulationDTO));
        model.addAttribute("simulationDTO", simulationDTO);
        return "simulation/lifeTime";
    }
}
