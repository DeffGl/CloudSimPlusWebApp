package com.example.cloudsimpluswebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
public class SimulationResultDTO {
    //TODO Дублирование данных, будет время надо будет придумать как вытаскивать характеристики ресурсов из симуляции по их номерам

    private long cloudletNumber;
    private long hostNumber;
    private long vmNumber;
    private long datacenterBrokerNumber;

    private long hostPes;
    private long vmPes;
    private long cloudletLength;
    private long finishedLength;
    private long cloudletPes;

    private String status;
    private double startTime;
    private double finishTime;
    private double executionTime;
}
