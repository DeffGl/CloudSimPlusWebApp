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

    @Column(name = "vm_count")
    private int vmCount;

    @Column(name = "vm_pes")
    private int vmPes;

    @Column(name = "vm_ram")
    private long vmRam;

    @Column(name = "vm_bw")
    private long vmBw;

    @Column(name = "vm_storage")
    private long vmStorage;

    @Column(name="vm_lifetime")
    private double vmLifetime;
}
