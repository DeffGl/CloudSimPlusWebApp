package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "vm", schema = "public")
@Data
@Accessors(chain = true)
public class Vm {
    @Id
    @Column(name = "vm_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "vms_count")
    private int vmsCount;

    @Column(name = "vm_pes")
    private int vmPes;

    @ManyToOne
    @JoinColumn(name = "simulation_id", referencedColumnName = "simulation_id")
    private Simulation simulation;
}
