package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.Simulation;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VmDTO {
    private int vmsCount;
    private int vmPes;
    private Simulation simulation;
}
