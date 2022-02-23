package com.cg.bms.bookingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.model.Seats;
import com.cg.bms.bookingservice.repository.BookingRepository;
import com.cg.bms.bookingservice.service.BookingService;


@RestController
public class BookingController {
	
	
	@Autowired
	private BookingService bookingService;
	
	
	@PostMapping("/bookings")
	public ResponseEntity<Booking> makeBooking(@RequestBody Booking booking)
	{
		return new ResponseEntity<>(bookingService.makeBooking(booking), HttpStatus.CREATED);
	}
	
	@GetMapping("/{bookingId}/bookings")
	public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId") String bookingId)
	{
		return new ResponseEntity<>(bookingService.getBooking(bookingId), HttpStatus.OK);
	}
	
	@GetMapping("/{bookingId}/availableSeats")
	public ResponseEntity<Seats> getAvailableSeatsForBookingShow(String bookingId)
	{
		return new ResponseEntity<>(bookingService.getAllAvailableSeatsForBookingShow(bookingId), HttpStatus.OK);
	}
	
	
	@PutMapping("/{bookingId}/bookings")
	public ResponseEntity<Booking> updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking updatedBooking)
	{
		return new ResponseEntity<>(bookingService.updateBooking(bookingId, updatedBooking), HttpStatus.OK);
	}
	
	@DeleteMapping("/{bookingId}/bookings")
	public ResponseEntity<Booking> cancelBooking(@PathVariable("bookingId") String bookingId)
	{
		return new ResponseEntity<>(bookingService.deleteBooking(bookingId), HttpStatus.OK);

	}
	
	
        
    
    
}
