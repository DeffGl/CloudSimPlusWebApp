package com.example.cloudsimpluswebapp.security;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.models.PersonCredential;
import com.example.cloudsimpluswebapp.services.PersonCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentPersonResolver {

    private final PersonCredentialService personCredentialService;

    @Autowired
    public CurrentPersonResolver( PersonCredentialService personCredentialService) {
        this.personCredentialService = personCredentialService;
    }

    public Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof PersonCredentialDetails userDetails) {
                PersonCredential personCredential = personCredentialService.getPersonCredentialByUsername(userDetails.getUsername()).orElseThrow();
                return personCredential.getPerson();
            } else {
                throw new IllegalStateException("Principal не является объектом PersonCredentialDetails");
            }
        } else {

            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
    }
}
