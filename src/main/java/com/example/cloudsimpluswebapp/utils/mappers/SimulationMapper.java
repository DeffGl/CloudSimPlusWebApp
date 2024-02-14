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

    @Mappings({
            // Маппинг для списка хостов
            @Mapping(target = "hosts", source = "hostDTOS"),
            // Маппинг для списка облачных задач
            @Mapping(target = "cloudlets", source = "cloudletDTOS"),
    })
    public abstract Simulation map(SimulationDTO simulationDTO);
    @Mappings({
            // Маппинг для списка хостов
            @Mapping(target = "hostDTOS", source = "hosts"),
            // Маппинг для списка облачных задач
            @Mapping(target = "cloudletDTOS", source = "cloudlets"),
    })
    public abstract SimulationDTO map(Simulation simulation);
    @Mappings({
            // Маппинг для списка хостов
            @Mapping(target = "hosts", source = "hostDTOS"),
            // Маппинг для списка облачных задач
            @Mapping(target = "cloudlets", source = "cloudletDTOS"),
    })
    public abstract void update(SimulationDTO simulationDTO, @MappingTarget Simulation simulation);
}
