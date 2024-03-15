package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;

public enum SimulationType {
    BASIC_SIMULATION("/basic"),
    LIFETIME_SIMULATION("/lifetime"),
    CANCEL_SIMULATION("/cancel");

    private final String url;

    SimulationType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return "/simulation" + url;
    }
}
