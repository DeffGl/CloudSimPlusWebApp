package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "cloudlet", schema = "public")
@Data
@Accessors(chain = true)
public class Cloudlet {
    @Id
    @Column(name = "cloudlet_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "cloudlets_count")
    private int cloudletsCount;

    @Column(name = "cloudlet_pes")
    private int cloudletPes;

    @Column(name = "cloudlet_length")
    private int cloudletLength;

    @ManyToOne
    @JoinColumn(name = "simulation_id", referencedColumnName = "simulation_id")
    private Simulation simulation;
}
