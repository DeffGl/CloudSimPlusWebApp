package com.example.cloudsimpluswebapp.controllers.exceptions;

import com.example.cloudsimpluswebapp.controllers.SimulationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(assignableTypes = SimulationController.class)
public class SimulationExceptionController {
    private static final Logger log = LoggerFactory.getLogger(SimulationExceptionController.class);

}
