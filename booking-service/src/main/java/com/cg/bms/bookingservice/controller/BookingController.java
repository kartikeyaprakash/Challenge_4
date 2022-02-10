package com.cg.bms.bookingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.repository.BookingRepository;


@RestController
@RequestMapping("/api/booking")
public class BookingController {
	
	
	@Autowired
	private BookingRepository bookingRepository;
	
	
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBooking(@RequestBody Booking theatre) {
    	bookingRepository.save(theatre);
    }
    
    
    
}
