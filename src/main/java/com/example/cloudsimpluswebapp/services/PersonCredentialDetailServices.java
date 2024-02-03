package com.example.cloudsimpluswebapp.services;

import com.example.cloudsimpluswebapp.models.PersonCredential;
import com.example.cloudsimpluswebapp.repositories.PersonCredentialRepository;
import com.example.cloudsimpluswebapp.security.PersonCredentialDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonCredentialDetailServices implements UserDetailsService {

    private final PersonCredentialRepository personCredentialRepository;

    @Autowired
    public PersonCredentialDetailServices(PersonCredentialRepository personCredentialRepository) {
        this.personCredentialRepository = personCredentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonCredential personCredential = personCredentialRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Not found person credentials with username %s", username)));
        return new PersonCredentialDetails(personCredential);
    }
}
