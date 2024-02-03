package com.example.cloudsimpluswebapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "person", schema = "public")
@Data
@Accessors(chain = true)
public class Person {
    @Id
    @Column(name = "person_id")
    private UUID id;

    @Column(name = "name")
    private String name;
}
