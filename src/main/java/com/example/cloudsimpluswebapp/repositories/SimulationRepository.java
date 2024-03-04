package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<Simulation, UUID> {
    List<Simulation> getSimulationsByPerson(Person person);
}
