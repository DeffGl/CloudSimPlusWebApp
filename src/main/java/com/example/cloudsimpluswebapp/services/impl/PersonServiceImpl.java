package com.example.cloudsimpluswebapp.services.impl;

import com.example.cloudsimpluswebapp.models.Person;
import com.example.cloudsimpluswebapp.repositories.PersonRepository;
import com.example.cloudsimpluswebapp.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void savePerson(Person person) {
        personRepository.save(person);
    }
}
