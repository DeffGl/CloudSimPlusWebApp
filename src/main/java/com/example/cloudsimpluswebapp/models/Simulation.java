package com.example.cloudsimpluswebapp.models;

import com.example.cloudsimpluswebapp.models.enums.SimulationType;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private List<Host> hosts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private List<Cloudlet> cloudlets;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private List<SimulationResult> simulationResults;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private List<Datacenter> datacenters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private List<DatacenterBroker> datacenterBrokers;

    @Column(name = "simulation_type")
    @Enumerated(EnumType.STRING)
    private SimulationType simulationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

}
