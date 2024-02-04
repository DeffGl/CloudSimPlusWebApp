package com.example.cloudsimpluswebapp.utils.validators;

import com.example.cloudsimpluswebapp.dto.PersonCredentialDTO;
import com.example.cloudsimpluswebapp.models.PersonCredential;
import com.example.cloudsimpluswebapp.services.PersonCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonCredentialsValidator implements Validator {

    private final PersonCredentialService personCredentialService;

    @Autowired
    public PersonCredentialsValidator(PersonCredentialService personCredentialService) {
        this.personCredentialService = personCredentialService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonCredential.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonCredentialDTO personCredentialDTO = (PersonCredentialDTO) target;
        Optional<PersonCredential> personCredential = personCredentialService.getPersonCredentialByUsername(personCredentialDTO.getUsername());
        if (personCredential.isPresent()){
            errors.rejectValue("username", "", "Пользователь с таким логином уже существует.");
        }
    }
}
