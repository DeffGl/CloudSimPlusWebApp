package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
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

    @Column(name = "host_count")
    private int hostCount;

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
//TODO Нужно реализовать корректное отображение виртуальных машин и работу с ними на фронте
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private List<Vm> vms;
}
