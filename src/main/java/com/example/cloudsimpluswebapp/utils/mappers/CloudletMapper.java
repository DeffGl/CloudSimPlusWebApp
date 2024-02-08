package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.CloudletDTO;
import com.example.cloudsimpluswebapp.models.Cloudlet;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CloudletMapper {
    public abstract Cloudlet map(CloudletDTO cloudletDTO);
    public abstract CloudletDTO map(Cloudlet cloudlet);
    public abstract void update(CloudletDTO cloudletDTO, @MappingTarget Cloudlet cloudlet);
}
