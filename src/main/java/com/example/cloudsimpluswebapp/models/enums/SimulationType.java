package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;

@Getter
public enum SimulationType {
    BASIC_SIMULATION("/simulation/basic"),
    LIFETIME_SIMULATION("/simulation/lifetime");

    private final String url;

    SimulationType(String url) {
        this.url = url;
    }
}
