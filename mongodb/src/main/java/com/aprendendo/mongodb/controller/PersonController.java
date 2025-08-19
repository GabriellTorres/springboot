package com.aprendendo.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendendo.mongodb.dto.PersonDto;
import com.aprendendo.mongodb.entity.Person;
import com.aprendendo.mongodb.repository.PersonRepository;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(@Autowired PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping("/save")
    public String savePerson(@RequestBody PersonDto personDto) {

        if(personDto.nome() == null || personDto.idade() <= 0) {
            throw new IllegalArgumentException("Invalid person data");
        }
        Person person = new Person();
        person.setName(personDto.nome());
        person.setAge(personDto.idade());

        personRepository.save(person);
        
        return "Person saved successfully!";
    }
    
    @GetMapping("/all")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }


}
