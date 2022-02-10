package com.cg.bms.bookingservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cg.bms.bookingservice.model.Booking;


public interface BookingRepository extends MongoRepository<Booking, String> {

}
