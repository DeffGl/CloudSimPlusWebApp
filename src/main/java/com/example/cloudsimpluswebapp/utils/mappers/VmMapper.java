package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.VmDTO;
import com.example.cloudsimpluswebapp.models.Vm;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class VmMapper {
    public abstract Vm map(VmDTO vmDTO);
    public abstract VmDTO map(Vm vm);
    public abstract void update(VmDTO vmDTO, @MappingTarget Vm vm);
}
