package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.SimulationResultDTO;
import com.example.cloudsimpluswebapp.models.SimulationResult;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SimulationResultMapper {
    public abstract SimulationResult map(SimulationResultDTO simulationResultDTO);
    public abstract SimulationResultDTO map(SimulationResult simulationResult);
    public abstract void update(SimulationResultDTO simulationResultDTO, @MappingTarget SimulationResult simulationResult);
    public SimulationResultDTO map(Cloudlet finishedList){
        return new SimulationResultDTO(
                finishedList.getId(),
                finishedList.getVm().getHost().getId(),
                finishedList.getVm().getId(),
                finishedList.getBroker().getId(),
                finishedList.getStatus().name(),
                finishedList.getStartTime(),
                finishedList.getFinishTime(),
                finishedList.getTotalExecutionTime());
    }

}
