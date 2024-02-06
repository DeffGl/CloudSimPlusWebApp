package com.example.cloudsimpluswebapp.models;

import com.example.cloudsimpluswebapp.models.enums.SimulationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "simulation", schema = "public")
@Data
@Accessors(chain = true)
public class Simulation {
    @Id
    @Column(name = "simulation_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "name_simulation")
    private String nameSimulation;

    @Column(name = "date_of_creation")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dateOfCreation;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.LAZY)
    private List<Host> hosts;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.LAZY)
    private List<Cloudlet> cloudlets;

    @OneToMany(mappedBy = "simulation", fetch = FetchType.LAZY)
    private List<Vm> vms;

    @Column(name = "simulation_status")
    @Enumerated(EnumType.STRING)
    private SimulationStatus simulationStatus;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;
}
