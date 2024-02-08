package com.example.cloudsimpluswebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String redirectToSimulation(){
        return "redirect:simulation";
    }
}
