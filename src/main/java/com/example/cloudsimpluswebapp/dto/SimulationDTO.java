package com.example.cloudsimpluswebapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class SimulationDTO {
    private String nameSimulation;
    private Date dateOfCreation = new Date();
    private List<HostDTO> hostDTOS = new ArrayList<>();
    private List<CloudletDTO> cloudletDTOS = new ArrayList<>();
    private List<SimulationResultDTO> simulationResultDTOS = new ArrayList<>();
    private List<DatacenterDTO> datacenterDTOS = new ArrayList<>();
    private List<DatacenterBrokerDTO> datacenterBrokerDTOS = new ArrayList<>();
}
