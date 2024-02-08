package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.models.Simulation;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SimulationMapper {
    public abstract Simulation map(SimulationDTO simulationDTO);
    public abstract SimulationDTO map(Simulation simulation);
    public abstract void update(SimulationDTO simulationDTO, @MappingTarget Simulation simulation);
}
