package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.Simulation;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import jakarta.persistence.TemporalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<Simulation, UUID> {
    List<Simulation> getSimulationsByPerson(Person person);
    Page<Simulation> getSimulationsByPerson(Person person, Pageable pageable);
    Page<Simulation> getSimulationsByPersonAndSimulationRemoved(Person person, Pageable pageable, boolean simulationRemoved);
    long countSimulationsByPerson(Person currentPerson);
    long countSimulationsByPersonAndSimulationRemoved(Person currentPerson, boolean simulationRemoved);
    @Query("SELECT COUNT(s) FROM Simulation s " +
            "WHERE (s.person = :currentPerson)" +
            "AND ((cast(:nameSimulation as string) IS NULL ) OR LOWER(s.nameSimulation) = LOWER((cast(:nameSimulation as string)))) " +
            "AND (cast(:dateOfCreation as date) IS NULL OR s.dateOfCreation = (:dateOfCreation)) " +
            "AND (:simulationType IS NULL OR s.simulationType = :simulationType) " +
            "AND (:simulationRemoved IS NULL OR s.simulationRemoved = :simulationRemoved)")
    long getCountSimulations(
            @Param("currentPerson") Person currentPerson,
            @Param("nameSimulation") String nameSimulation,
            @Param("dateOfCreation") @Temporal(TemporalType.DATE) Date dateOfCreation,
            @Param("simulationType") SimulationType simulationType,
            @Param("simulationRemoved") Boolean simulationRemoved);

    @Query("SELECT s FROM Simulation s " +
            "WHERE (s.person = :currentPerson)" +
            "AND ((cast(:nameSimulation as string) IS NULL ) OR LOWER(s.nameSimulation) = LOWER((cast(:nameSimulation as string)))) " +
            "AND (cast(:dateOfCreation as date) IS NULL OR s.dateOfCreation = (:dateOfCreation)) " +
            "AND (:simulationType IS NULL OR s.simulationType = :simulationType) " +
            "AND (:simulationRemoved IS NULL OR s.simulationRemoved = :simulationRemoved)")
    Page<Simulation> getSimulations(
            @Param("currentPerson") Person currentPerson,
            @Param("nameSimulation") String nameSimulation,
            @Param("dateOfCreation") @Temporal(TemporalType.DATE) Date dateOfCreation,
            @Param("simulationType") SimulationType simulationType,
            @Param("simulationRemoved") Boolean simulationRemoved,
            Pageable pageable);
}
