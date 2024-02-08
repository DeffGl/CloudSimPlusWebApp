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
    public abstract Host map(HostDTO hostDTO);
    public abstract HostDTO map(Host host);
    public abstract void update(HostDTO hostDTO, @MappingTarget Host host);
}
