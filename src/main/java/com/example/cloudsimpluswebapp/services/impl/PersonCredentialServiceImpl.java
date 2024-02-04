package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.models.PersonCredential;
import com.example.cloudsimpluswebapp.repositories.PersonCredentialRepository;
import com.example.cloudsimpluswebapp.services.PersonCredentialService;
import com.example.cloudsimpluswebapp.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonCredentialServiceImpl implements PersonCredentialService {
    private final PersonCredentialRepository personCredentialRepository;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonCredentialServiceImpl(PersonCredentialRepository personCredentialRepository, PersonService personService, PasswordEncoder passwordEncoder) {
        this.personCredentialRepository = personCredentialRepository;
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<PersonCredential> getPersonCredentialByUsername(String username) {
        return personCredentialRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void registerCredential(PersonCredential personCredential) {
        personCredential.setPassword(passwordEncoder.encode(personCredential.getPassword()));
        personCredentialRepository.save(personCredential);
    }
}
