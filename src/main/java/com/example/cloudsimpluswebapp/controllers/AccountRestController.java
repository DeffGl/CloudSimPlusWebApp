package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.services.SimulationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/account")
public class AccountRestController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final ObjectMapper objectMapper;

    private final SimulationService simulationService;

    @Autowired
    public AccountRestController(ObjectMapper objectMapper, SimulationService simulationService) {
        this.objectMapper = objectMapper;
        this.simulationService = simulationService;
    }

    @GetMapping
    public ResponseEntity<String> getTable(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "") String nameSimulation,
                                           @RequestParam(required = false, defaultValue = "") String dateOfCreation,
                                           @RequestParam(required = false, defaultValue = "") String simulationType,
                                           @RequestParam(required = false, defaultValue = "false") boolean simulationRemoved) throws JsonProcessingException, ParseException {


        //Page<SimulationDTO> simulationDTOS = simulationService.getSimulationsByPerson(page);
        Page<SimulationDTO> simulationDTOS = simulationService.getSimulationsByPersonAndFilter(
                page,
                nameSimulation,
                dateOfCreation,
                simulationType,
                simulationRemoved);


        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", simulationDTOS.getTotalPages());
        response.put("page", page);
        response.put("simulationDTOS", simulationDTOS.getContent());

        String jsonResponse = objectMapper.writeValueAsString(response);

        return ResponseEntity.ok(jsonResponse);
    }
}
