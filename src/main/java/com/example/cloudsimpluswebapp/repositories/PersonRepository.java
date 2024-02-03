package com.example.cloudsimpluswebapp.repositories;

import com.example.cloudsimpluswebapp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PersonRepository extends JpaRepository<Person, UUID>{
}
