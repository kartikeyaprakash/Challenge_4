package com.cg.bms.bookingservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.repository.BookingRepository;



@Service
@Transactional
public class BookingService {
	
	/**
	 * Check if list of seats are available in showTime selected
	 * Make booking: initialize list of seats to showTime
	 * 				 Lock the seats in theatre service for showTime, if available
	 * 
	 * Cancel Booking: Release the locks on seats for showTime
	 * 
	 * Update Booking : Update the booking
	 * 					for seat updation, check availability again 
	 * 
	 * Get Bookings: Only interaction with booking DB done here, no requirement for other service calls
	 * 
	 * 
	 */
	
	
	
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Booking makeBooking(Booking booking)
	{
		return bookingRepository.save(booking);
	}
	
	public Booking getBooking(String bookingId)
	{
		Optional<Booking> foundBooking = bookingRepository.findById(bookingId);
		return foundBooking.isPresent() ? foundBooking.get() : null;
	}
	
	
	//For history, use booking date
	public List<Booking> getBookingsForUser(String userId)
	{	
		return bookingRepository.findByUserId(userId);
	}
	
	
	
	//can use an updateBookingDTO here
	public Booking updateBooking(String bookingId, Booking updatedBooking)
	{
		
		//only date and movieId is updated as of now
		Booking booking = Booking.builder()._id(bookingId).bookingDate(updatedBooking.getBookingDate()).movieId(updatedBooking.getMovieId()).build();
		return bookingRepository.save(booking);
	}
	
	public Booking deleteBooking(String bookingId)
	{
		
		//Seat logic here
		Booking bookingToBeDeleted = getBooking(bookingId);
		bookingRepository.deleteById(bookingId);
		return bookingToBeDeleted;
	}
	
	public List<Booking> findBookingBeforeDateForUser(String userId, Date bookingDate)
	{
		return bookingRepository.findByUserIdAndBeforeBookingDate(userId, bookingDate);
	}
	
	
	public List<Booking> getAllBookingForUser(String userId)
	{
		return bookingRepository.findByUserId(userId);
	}
	
	public List<Booking> findBookingAfterDateForUser(String userId, Date thresholdDate)
	{
		return bookingRepository.findByUserIdAndAfterBookingDate(userId, thresholdDate);
	}
	
	
	
	
	

}
