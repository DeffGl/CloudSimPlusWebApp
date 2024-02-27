package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "datacenter_broker", schema = "public")
@Data
@Accessors(chain = true)
public class DatacenterBroker {
    @Id
    @Column(name = "broker_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "broker_count")
    private int brokerCount;
}
