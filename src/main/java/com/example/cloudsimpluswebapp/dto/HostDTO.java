package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.Simulation;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HostDTO {
    //TODO Сделать валидацию
    private int hostsCount;
    private int hostPes;
    private long hostMips;
    private long hostRam;
    private long hostBw;
    private long hostStorage;
    private Simulation simulation;
}
