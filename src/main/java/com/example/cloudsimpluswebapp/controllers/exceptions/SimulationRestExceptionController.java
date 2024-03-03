package com.example.cloudsimpluswebapp.controllers.exceptions;

import com.example.cloudsimpluswebapp.controllers.SimulationRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = SimulationRestController.class)
public class SimulationRestExceptionController {
    private static final Logger log = LoggerFactory.getLogger(SimulationExceptionController.class);

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleUndefinedException(Exception ex) {
        log.error(String.format("Произошла непредвиденная ошибка: %s", ex.getMessage()));
        ex.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Произошла непредвиденная ошибка на сервере. Обратитесь в службу поддержки.");
        return ResponseEntity.internalServerError().body(response);
    }
}
