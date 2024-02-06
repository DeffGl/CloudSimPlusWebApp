package com.example.cloudsimpluswebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    @GetMapping
    public String getMainPage(){
        return "simulation/main";
    }

    @GetMapping("/basic")
    public String getBasicSimulationPage(){
        return "simulation/basic";
    }
}
