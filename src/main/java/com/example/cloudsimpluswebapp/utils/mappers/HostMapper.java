package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.HostDTO;
import com.example.cloudsimpluswebapp.models.Host;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class HostMapper {
    @Mappings({
            @Mapping(target = "vms", source = "vmDTOS")
    })
    public abstract Host map(HostDTO hostDTO);
    @Mappings({
            @Mapping(target = "vmDTOS", source = "vms")
    })
    public abstract HostDTO map(Host host);
    @Mappings({
            @Mapping(target = "vms", source = "vmDTOS")
    })
    public abstract void update(HostDTO hostDTO, @MappingTarget Host host);
}
