package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DatacenterDTO {
    private int datacenterCount;
    private int schedulingInterval;
    private double hostMeanFailureNumberPerHour;
    private double maxTimeToFailInHours;
}
