package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    private static final Logger log = LoggerFactory.getLogger(SimulationController.class);

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
        SimulationDTO simulationDTO = new SimulationDTO();
        String simulationDTOJson = objectMapper.writeValueAsString(simulationDTO);
        log.info(simulationDTOJson);
        model.addAttribute("simulationDTOJson", simulationDTOJson);
        model.addAttribute("simulationDTO", simulationDTO);
        return "simulation/basic";
    }
}
