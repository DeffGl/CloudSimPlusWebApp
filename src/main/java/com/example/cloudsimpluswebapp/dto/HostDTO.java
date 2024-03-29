package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class HostDTO {
    //TODO Сделать валидацию

    private int hostCount;
    private int hostPes;
    private long hostMips;
    private long hostRam;
    private long hostBw;
    private long hostStorage;
    private List<VmDTO> vmDTOS = new ArrayList<>();
}
