package com.aprendendo.encurtadorurl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aprendendo.encurtadorurl.entity.Url;

public interface UrlRepository extends MongoRepository<Url, String> {
    
}
