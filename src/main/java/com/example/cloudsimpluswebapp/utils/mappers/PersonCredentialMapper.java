package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.PersonCredentialDTO;
import com.example.cloudsimpluswebapp.dto.PersonDTO;
import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.PersonCredential;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PersonCredentialMapper {
    public abstract PersonCredential map(PersonCredentialDTO personCredentialDTO);
    public abstract PersonCredentialDTO map(PersonCredential personCredential);
    public abstract void update(PersonCredentialDTO personCredentialDTO, @MappingTarget PersonCredential personCredential);
}
