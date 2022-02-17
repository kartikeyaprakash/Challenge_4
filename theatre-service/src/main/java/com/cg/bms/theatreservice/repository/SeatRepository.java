package com.cg.bms.theatreservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.theatreservice.model.Seat;

@Repository
public interface SeatRepository extends MongoRepository<Seat, String> {

}
