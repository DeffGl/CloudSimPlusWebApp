package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.PersonDTO;
import com.example.cloudsimpluswebapp.models.Person;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PersonMapper {
    public abstract Person map(PersonDTO personDTO);
    public abstract PersonDTO map(Person person);
    public abstract void update(PersonDTO personDTO, @MappingTarget Person person);
}
