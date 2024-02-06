package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "host", schema = "public")
@Data
@Accessors(chain = true)
public class Host {
    @Id
    @Column(name = "host_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "hosts_count")
    private int hostsCount;

    @Column(name = "host_pes")
    private int hostPes;

    @Column(name = "host_mips")
    private long hostMips;

    @Column(name = "host_ram")
    private long hostRam;

    @Column(name = "host_bw")
    private long hostBw;

    @Column(name = "host_storage")
    private long hostStorage;

    @ManyToOne
    @JoinColumn(name = "simulation_id", referencedColumnName = "simulation_id")
    private Simulation simulation;
}
