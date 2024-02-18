package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "sumulation_result", schema = "public")
@Data
@Accessors(chain = true)
public class SimulationResult {
    @Id
    @Column(name = "simulation_result_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "status")
    private String status;

    @Column (name = "start_time")
    private double startTime;

    @Column (name = "finish_time")
    private double finishTime;

    @Column(name = "execution_time")
    private double executionTime;
}
