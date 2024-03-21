package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.Simulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<Simulation, UUID> {
    List<Simulation> getSimulationsByPerson(Person person);
    Page<Simulation> getSimulationsByPerson(Person person, Pageable pageable);
    long countSimulationsByPerson(Person currentPerson);
}
