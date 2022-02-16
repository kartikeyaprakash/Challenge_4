package com.cg.bms.cityservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.cityservice.model.City;

@Repository
public interface CityRepository extends MongoRepository<City, String>{

}
