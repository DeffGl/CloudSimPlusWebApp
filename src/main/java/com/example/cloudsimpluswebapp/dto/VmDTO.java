package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VmDTO {
    private int vmCount;
    private int vmPes;
    private long vmRam;
    private long vmBw;
    private long vmStorage;
    private double vmLifetime;
    private double vmBootDelay;
    private double vmShutdownDelay;
}
