package com.example.cloudsimpluswebapp.utils.exceptions;

public class SimulationException extends Exception{
    public SimulationException() {
    }

    public SimulationException(String message) {
        super(message);
    }

    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimulationException(Exception e) {
    }
}
