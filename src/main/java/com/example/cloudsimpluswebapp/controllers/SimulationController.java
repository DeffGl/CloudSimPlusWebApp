package com.example.cloudsimpluswebapp.controllers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    @GetMapping
    public String getMainPage(){
        return "simulation/main";
    }

    @GetMapping("/basic")
    public String getBasicSimulationPage(@ModelAttribute("simulationDTO") SimulationDTO simulationDTO) {
        return "simulation/basic";
    }

    @PostMapping("/start")
    public void startSimulation(@ModelAttribute("simulationDTO") @Validated SimulationDTO simulationDTO, BindingResult bindingResult){
        //TODO Исправить проблему с возвращаемым значением  Error resolving template [simulation/start]
        System.out.println(simulationDTO.toString());
    }
}
