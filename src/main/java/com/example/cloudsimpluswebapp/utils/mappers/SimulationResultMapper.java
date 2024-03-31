package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.dto.SimulationResultDTO;
import com.example.cloudsimpluswebapp.models.SimulationResult;
import com.example.cloudsimpluswebapp.models.enums.ResultStatusType;
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
    public SimulationResultDTO map(Cloudlet result, CloudletDTO cloudletDTO){
        Vm vm = result.getVm();
        Host host = vm.getHost();
        Datacenter datacenter = result.getLastTriedDatacenter();
        return new SimulationResultDTO(
                result.getId(),
                host.getId(),
                vm.getId(),
                datacenter.getId(),
                host.getPesNumber(),
                vm.getPesNumber(),
                result.getLength(),
                result.getFinishedLengthSoFar(),
                result.getPesNumber(),
                ResultStatusType.getLocalizationNameByStatus(result.getStatus().name()),
                result.getStartTime(),
                result.getFinishTime(),
                cloudletDTO.getCloudletMaxExecutionTime(),
                result.getTotalExecutionTime(),
                result.getLifeTime(),
                vm.getLifeTime());
    }

}
