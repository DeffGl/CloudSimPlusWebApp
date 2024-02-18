package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.HostDTO;
import com.example.cloudsimpluswebapp.dto.SimulationDTO;
import com.example.cloudsimpluswebapp.dto.VmDTO;
import com.example.cloudsimpluswebapp.models.Host;
import com.example.cloudsimpluswebapp.models.Simulation;
import com.example.cloudsimpluswebapp.models.Vm;
import org.mapstruct.*;

import java.util.stream.Collectors;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class SimulationMapper {

    @Mappings({
            @Mapping(target = "hosts", source = "hostDTOS"),
            @Mapping(target = "cloudlets", source = "cloudletDTOS"),
            @Mapping(target = "simulationResults", source = "simulationResultDTOS")
    })
    public abstract Simulation map(SimulationDTO simulationDTO);
    @Mappings({
            @Mapping(target = "hostDTOS", source = "hosts"),
            @Mapping(target = "cloudletDTOS", source = "cloudlets"),
            @Mapping(target = "simulationResultDTOS", source = "simulationResults")
    })
    public abstract SimulationDTO map(Simulation simulation);
    @Mappings({
            @Mapping(target = "hosts", source = "hostDTOS"),
            @Mapping(target = "cloudlets", source = "cloudletDTOS"),
            @Mapping(target = "simulationResults", source = "simulationResultDTOS")
    })
    public abstract void update(SimulationDTO simulationDTO, @MappingTarget Simulation simulation);

    @AfterMapping
    protected void addVmDTOsToHostDTO(@MappingTarget HostDTO hostDTO, Host host) {
        hostDTO.setVmDTOS(host.getVms().stream()
                .map(this::vmToVmDTO)
                .collect(Collectors.toList()));
    }
    protected abstract VmDTO vmToVmDTO(Vm vm);

    @AfterMapping
    protected void addVmDTOsToHost(@MappingTarget Host host, HostDTO hostDTO) {
        host.setVms(hostDTO.getVmDTOS().stream()
                .map(this::vmDTOToVm)
                .collect(Collectors.toList()));
    }

    protected abstract Vm vmDTOToVm(VmDTO vmDTO);
}
