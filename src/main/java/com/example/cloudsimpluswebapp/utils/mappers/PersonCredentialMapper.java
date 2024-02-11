package com.example.cloudsimpluswebapp.utils.mappers;

import com.example.cloudsimpluswebapp.dto.PersonCredentialDTO;
import com.example.cloudsimpluswebapp.models.PersonCredential;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PersonCredentialMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "person.name", source = "personDTO.name"),
            @Mapping(target = "person.email", source = "personDTO.email"),
    })
    public abstract PersonCredential map(PersonCredentialDTO personCredentialDTO);
    public abstract PersonCredentialDTO map(PersonCredential personCredential);
    public abstract void update(PersonCredentialDTO personCredentialDTO, @MappingTarget PersonCredential personCredential);
}
