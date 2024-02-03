package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.PersonCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PersonCredentialRepository extends JpaRepository<PersonCredential, UUID> {
    Optional<PersonCredential> findByUsername(String username);
}
