package com.example.cloudsimpluswebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@AllArgsConstructor
public class SimulationResultDTO {

    private long cloudletId;
    private long hostId;
    private long vmId;
    private long datacenterBrokerId;

    private String status;
    private double startTime;
    private double finishTime;
    private double executionTime;
}
