package com.example.cloudsimpluswebapp.models.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum ResultStatusType {
    SUCCESS("Успешно"),
    CANCELED("Отменено"),
    RESULT_EMPTY("Результат отсутствует");

    private final String localizationName;

    ResultStatusType(String localizationName) {
        this.localizationName = localizationName;
    }

    public static String getLocalizationNameByStatus(String status){
        System.out.println(status);
        return Arrays.stream(ResultStatusType.values())
                .filter(resultStatusType -> resultStatusType.name().equals(status))
                .findAny()
                .orElse(RESULT_EMPTY)
                .getLocalizationName();
    }

}
