package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.DatacenterBrokerDTO;
import com.example.cloudsimpluswebapp.models.DatacenterBroker;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class DatacenterBrokerMapper {
    public abstract DatacenterBroker map(DatacenterBrokerDTO datacenterBrokerDTO);
    public abstract DatacenterBrokerDTO map(DatacenterBroker datacenterBroker);
    public abstract void update(DatacenterBrokerDTO datacenterBrokerDTO, @MappingTarget DatacenterBroker datacenterBroker);
}
