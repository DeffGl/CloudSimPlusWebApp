package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final ObjectMapper objectMapper;

    private final SimulationService simulationService;

    @Autowired
    public AccountController(ObjectMapper objectMapper, SimulationService simulationService) {
        this.objectMapper = objectMapper;
        this.simulationService = simulationService;
    }

    @GetMapping
    public String getAccountPage(Model model) throws JsonProcessingException {
        List<SimulationDTO> simulationDTOS = simulationService.getSimulationsByPerson();
        model.addAttribute("simulationDTOJson", objectMapper.writeValueAsString(simulationDTOS));
        model.addAttribute("simulationDTOS", simulationDTOS);
        return "/account";
    }
}
