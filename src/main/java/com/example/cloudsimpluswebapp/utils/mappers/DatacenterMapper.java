package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.DatacenterDTO;
import com.example.cloudsimpluswebapp.models.Datacenter;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class DatacenterMapper {
    public abstract Datacenter map(DatacenterDTO datacenterDTO);
    public abstract DatacenterDTO map(Datacenter datacenter);
    public abstract void update(DatacenterDTO datacenterDTO, @MappingTarget Datacenter datacenter);
}
