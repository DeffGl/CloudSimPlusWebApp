package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.Vm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class HostDTO {
    //TODO Сделать валидацию
    private int hostPes;
    private long hostMips;
    private long hostRam;
    private long hostBw;
    private long hostStorage;
    private List<VmDTO> vmDTOS;
}
