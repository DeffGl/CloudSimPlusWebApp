package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.Simulation;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CloudletDTO {
    //TODO Сделать валидацию
    private int cloudletsCount;
    private int cloudletPes;
    private int cloudletLength;
    private Simulation simulation;
}
