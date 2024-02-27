package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.SimulationResultDTO;
import com.example.cloudsimpluswebapp.models.SimulationResult;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.vms.Vm;
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
        Vm vm = finishedList.getVm();
        Host host = vm.getHost();
        Datacenter datacenter = finishedList.getLastTriedDatacenter();
        return new SimulationResultDTO(
                finishedList.getId(),
                host.getId(),
                vm.getId(),
                datacenter.getId(),
                host.getPesNumber(),
                vm.getPesNumber(),
                finishedList.getLength(),
                finishedList.getFinishedLengthSoFar(),
                finishedList.getPesNumber(),
                finishedList.getStatus().name(),
                finishedList.getStartTime(),
                finishedList.getFinishTime(),
                finishedList.getTotalExecutionTime(),
                finishedList.getLifeTime(),
                vm.getLifeTime());
    }

}
