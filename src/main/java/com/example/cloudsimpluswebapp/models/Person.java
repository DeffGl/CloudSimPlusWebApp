package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "person", schema = "public")
@Data
@Accessors(chain = true)
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private List<Simulation> simulation;
}
