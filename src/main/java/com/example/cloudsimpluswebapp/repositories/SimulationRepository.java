package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.Simulation;
import com.example.cloudsimpluswebapp.models.enums.SimulationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SimulationRepository extends JpaRepository<Simulation, UUID> {
    List<Simulation> getSimulationsByPerson(Person person);
    Page<Simulation> getSimulationsByPerson(Person person, Pageable pageable);
    long countSimulationsByPerson(Person currentPerson);
    long countSimulationsByPersonAndNameSimulationAndDateOfCreationAndSimulationTypeAndSimulationRemoved(@Param("currentPerson") Person currentPerson,
                                                                                                         @Nullable @Param("nameSimulation") String nameSimulation,
                                                                                                         @Nullable @Param("dateOfCreation") Date dateOfCreation,
                                                                                                         @Nullable @Param("simulationType") SimulationType simulationType,
                                                                                                         @Nullable @Param("simulationRemoved") Boolean simulationRemoved);
    Page<Simulation> getSimulationsByPersonAndNameSimulationAndDateOfCreationAndSimulationTypeAndSimulationRemoved( @Param("currentPerson") Person currentPerson,
                                                                                                                    @Nullable @Param("nameSimulation") String nameSimulation,
                                                                                                                    @Nullable @Param("dateOfCreation") Date dateOfCreation,
                                                                                                                    @Nullable @Param("simulationType") SimulationType simulationType,
                                                                                                                    @Nullable @Param("simulationRemoved") Boolean simulationRemoved,
                                                                                                                    Pageable pageable);
}
