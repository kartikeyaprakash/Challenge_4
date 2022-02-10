package com.cg.bms.theatreservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cg.bms.theatreservice.model.Theatre;

public interface TheatreRepository extends MongoRepository<Theatre, String> {

}
