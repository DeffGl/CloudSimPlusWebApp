package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.models.PersonCredential;

import java.util.Optional;

public interface PersonCredentialService {
    Optional<PersonCredential> getPersonCredentialByUsername(String username);

    void registerCredential(PersonCredential personCredential);
}
