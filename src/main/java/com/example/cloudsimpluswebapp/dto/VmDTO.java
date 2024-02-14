package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VmDTO {
    private int vmPes;
    private long vmRam;
    private long vmBw;
    private long vmStorage;
}
