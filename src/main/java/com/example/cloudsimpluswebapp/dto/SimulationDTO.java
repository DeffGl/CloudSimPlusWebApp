package com.example.cloudsimpluswebapp.dto;

import com.example.cloudsimpluswebapp.models.Cloudlet;
import com.example.cloudsimpluswebapp.models.Host;
import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.Vm;
import com.example.cloudsimpluswebapp.models.enums.SimulationStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class SimulationDTO {
    private UUID id;
    private String nameSimulation;
    private Date dateOfCreation;
    private List<Host> hosts;
    private List<Cloudlet> cloudlets;
    private List<Vm> vms;
    private SimulationStatus simulationStatus;
    private Person person;
}
