package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.enums.SimulationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class SimulationDTO {
    private String nameSimulation;
    private Date dateOfCreation;
    private List<HostDTO> hostDTOS;
    private List<CloudletDTO> cloudletDTOS;
    private List<VmDTO> vmDTOS;
    private SimulationStatus simulationStatus;
    private PersonDTO personDTO;
}
