package com.aprendendo.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aprendendo.mongodb.entity.Person;

public interface PersonRepository extends MongoRepository<Person, String> {}
