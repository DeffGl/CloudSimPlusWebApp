package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum ResultStatusType {
    SUCCESS("Успешно"),
    CANCELED("Отменено");

    private final String localizationName;

    ResultStatusType(String localizationName) {
        this.localizationName = localizationName;
    }

    public static String getLocalizationNameByStatus(String status){
        return Arrays.stream(ResultStatusType.values())
                .filter(resultStatusType -> resultStatusType.name().equals(status))
                .findAny()
                .orElseThrow()
                .getLocalizationName();
    }

}
