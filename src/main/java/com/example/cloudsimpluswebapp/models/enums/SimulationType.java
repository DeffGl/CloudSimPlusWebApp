package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;

@Getter
public enum SimulationType {
    BASIC_SIMULATION("/simulation/basic", "BASIC_SIMULATION"),
    LIFETIME_SIMULATION("/simulation/lifetime", "LIFETIME_SIMULATION");

    private final String url;
    private final String displayName;

    SimulationType(String url, String displayName) {
        this.url = url;
        this.displayName = displayName;
    }
}
