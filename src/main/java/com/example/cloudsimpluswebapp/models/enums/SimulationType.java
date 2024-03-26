package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum SimulationType {
    BASIC_SIMULATION("/basic", "Базовая симуляция"),
    LIFETIME_SIMULATION("/lifetime", "Lifetime симуляция"),
    CANCEL_SIMULATION("/cancel", "Симуляция отмены");

    private final String url;
    @Getter
    private final String localizationName;

    SimulationType(String url, String localizationName) {
        this.url = url;
        this.localizationName = localizationName;
    }

    public String getUrl() {
        return "/simulation" + url;
    }

    public static SimulationType getSimulationTypeByName(String simulationTypeName){
        if (simulationTypeName != null && !simulationTypeName.isEmpty()){
            return Arrays.stream(SimulationType.values()).filter(simulationType -> simulationType.name().equals(simulationTypeName)).findAny().orElseThrow();
        } else {
            return null;
        }
    }

    public static Map<String, Object> getSimulationTypes(){
        Map<String, Object> simulationTypes = new HashMap<>();
        Arrays.stream(SimulationType.values()).forEach(simulationType -> simulationTypes.put(simulationType.getLocalizationName(), simulationType.name()));
        return simulationTypes;
    }
}
