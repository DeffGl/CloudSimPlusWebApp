package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "datacenter", schema = "public")
@Data
@Accessors(chain = true)
public class Datacenter {
    @Id
    @Column(name = "datacenter_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "datacenter_count")
    private int datacenterCount;

    @Column(name="datacenter_scheduling_interval")
    private int schedulingInterval;
}
