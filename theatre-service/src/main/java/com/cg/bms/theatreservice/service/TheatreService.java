package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.model.Booking;
import com.cg.bms.theatreservice.model.City;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Seat;
import com.cg.bms.theatreservice.model.SeatStatus;
import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.model.Theatres;
import com.cg.bms.theatreservice.repository.ScreenRepository;
import com.cg.bms.theatreservice.repository.SeatRepository;
import com.cg.bms.theatreservice.repository.ShowTimeRepository;
import com.cg.bms.theatreservice.repository.TheatreRepository;

@Service
@Transactional
public class TheatreService {
	
	@Autowired
	private TheatreRepository theatreRepository;
	
	@Autowired
	private ScreenRepository screenRepository;
	
	@Autowired
	private ShowTimeRepository showTimeRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	
	
	public Theatre addTheatre(Theatre theatre)
	{
		Theatre addedTheatre = theatreRepository.save(theatre);
		addTheatreToCity(addedTheatre);
		return addedTheatre;
		
	}
	
	public Theatre getTheatre(String theatreId)
	{
		Optional<Theatre> foundTheatre = theatreRepository.findById(theatreId);
		if(foundTheatre.isPresent())
			return foundTheatre.get();
		
		//
		return null;
	}
	
	public Theatres getAllTheatres()
	{
		return Theatres.builder().theatres(theatreRepository.findAll()).build();
	}
	
	public City getCityOfTheatre(String theatreId)
	{
		
		String cityId = getTheatre(theatreId).getCityId();
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Accept", "application/json");
//		
//		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		return restTemplate.getForObject("http://city-service/{cityId}/cities", City.class, cityId);
		
		//return restTemplate.exchange("http://city-service/{cityId}/cities", HttpMethod.GET, httpEntity, City.class, cityId).getBody();
	}
	
	
	//Update Theatre DTO to be different, will not include City ID
	public Theatre updateTheatre(String theatreId, Theatre updatedTheatre)
	{
		updatedTheatre.set_id(theatreId);
		return theatreRepository.save(updatedTheatre);
	}
	
	public Theatre deleteTheatre(String theatreId)
	{
		Theatre theatreToBeDeleted = getTheatre(theatreId);
		removeTheatreFromCity(theatreId);
		for(String screenId: theatreToBeDeleted.getScreenIds())
			deleteScreen(screenId);
		theatreRepository.deleteById(theatreId);
		return theatreToBeDeleted;
	}
	
	
	public void addTheatreToCity(Theatre theatre)
	{
		City cityOfTheatre = getCityOfTheatre(theatre.get_id());
		List<String> theatreIds = cityOfTheatre.getTheatreIds();
		theatreIds.add(theatre.get_id());
		cityOfTheatre.setTheatreIds(theatreIds);
	
		restTemplate.put("http://city-service/{cityId}/cities", cityOfTheatre, theatre.getCityId());
	}
	
	
	public void removeTheatreFromCity(String theatreId)
	{
		City cityOfTheatre = getCityOfTheatre(theatreId);
		List<String> theatreIds = cityOfTheatre.getTheatreIds();
		theatreIds.remove(theatreId);
		cityOfTheatre.setTheatreIds(theatreIds);
	
		restTemplate.put("http://city-service/{cityId}/cities", cityOfTheatre, getTheatre(theatreId).getCityId());
	}
	
	public Theatres getTheatresByCity(String cityId)
	{
		City city = restTemplate.getForObject("http://city-service/{cityId}/cities", City.class, cityId);
		List<String> theatreIds = city.getTheatreIds();
		List<Theatre> theatres = new ArrayList<>();
		for(String theatreId: theatreIds)
		{
			theatres.add(getTheatre(theatreId));
		}
		return Theatres.builder().theatres(theatres).build();
	}
	
	
	public Screen addScreen(Screen screen)
	{
		Screen addedScreen =  screenRepository.save(screen);
		addScreenToTheatre(addedScreen);
		return addedScreen;
	}
	
	public void addScreenToTheatre(Screen screen)
	{
		Theatre parentTheatre= getTheatre(screen.getTheatreId());
		Set<String> screenIds = parentTheatre.getScreenIds();
		screenIds.add(screen.get_id());
		parentTheatre.setScreenIds(screenIds);
		updateTheatre(parentTheatre.get_id(), parentTheatre);
		
	}
	
	public Screen getScreen(String screenId)
	{
		Optional<Screen> foundScreen = screenRepository.findById(screenId);
		if(foundScreen.isPresent())
			return foundScreen.get();
		
		//custom exc
		return null;
	}
	
	public List<Screen> getScreensForTheatre(String theatreId)
	{
		List<Screen> result = new ArrayList<>();
		Set<String> screenIds = getTheatre(theatreId).getScreenIds();
		for(String screenId: screenIds)
			result.add(getScreen(screenId));
		return result;
	}
	
	public Screen updateScreen(String screenId, Screen updatedScreen)
	{
		
		//first check if screen exists for given screenId
		updatedScreen.set_id(screenId);
		return screenRepository.save(updatedScreen);
	}
	
	public void deleteScreenFromTheatre(String screenId)
	{
		Screen screenToDelete = getScreen(screenId);
		Theatre parentTheatre  = getTheatre(screenToDelete.getTheatreId());
		Set<String> screenIdsForTheatre = parentTheatre.getScreenIds();
		screenIdsForTheatre.remove(screenId);
		parentTheatre.setScreenIds(screenIdsForTheatre);
		updateTheatre(parentTheatre.get_id(), parentTheatre);
	}
	
	public Screen deleteScreen(String screenId)
	{
		Screen screenToDelete = getScreen(screenId);
		deleteScreenFromTheatre(screenId);
		for(String showTimeId: screenToDelete.getShowTimeIds())
			deleteShowTime(showTimeId);

		for(String seatId: screenToDelete.getSeatIds())
			deleteSeat(seatId);
		
		screenRepository.deleteById(screenId);
		return screenToDelete;
	}

	
	public ShowTime addShowTime(ShowTime showTime)
	{
		ShowTime addedShowTime = showTimeRepository.save(showTime);
		addShowTimeToScreen(addedShowTime);
		return addedShowTime;
		
	}
	
	public void addShowTimeToScreen(ShowTime showTime)
	{
		Screen parentScreen = getScreen(showTime.getScreenId());
		Set<String> showTimeIds = parentScreen.getShowTimeIds();
		showTimeIds.add(showTime.get_id());
		parentScreen.setShowTimeIds(showTimeIds);
		updateScreen(parentScreen.get_id(), parentScreen);
	}

	public ShowTime getShowTime(String showTimeId)
	{
		Optional<ShowTime> foundShowTime = showTimeRepository.findById(showTimeId);
		if(foundShowTime.isPresent())
			return foundShowTime.get();
		
		//custom exc
		return null;
	}
	
	public List<ShowTime> getShowTimesForScreen(String screenId)
	{
		List<ShowTime> showTimesForScreen = new ArrayList<>();
		for(String showTimeId: getScreen(screenId).getShowTimeIds())
			showTimesForScreen.add(getShowTime(showTimeId));
		return showTimesForScreen;
	}
	
	public ShowTime updateShowTime(String showTimeId, ShowTime updatedShowTime)
	{
		//first check if showTime exists for given id
		updatedShowTime.set_id(showTimeId);
		return showTimeRepository.save(updatedShowTime);
	}
	
	public void deleteShowTimeFromScreen(String showTimeId)
	{
		ShowTime showTimeToBeDeleted = getShowTime(showTimeId);
		Screen parentScreen = getScreen(showTimeToBeDeleted.getScreenId());
		Set<String> showTimeIds = parentScreen.getShowTimeIds();
		showTimeIds.remove(showTimeId);
		parentScreen.setShowTimeIds(showTimeIds);
		updateScreen(parentScreen.get_id(), parentScreen);
	}
	
	
	public ShowTime deleteShowTime(String showTimeId)
	{
		ShowTime showTimeToBeDeleted = getShowTime(showTimeId);
		deleteShowTimeFromScreen(showTimeId);
		for(String lockedSeatId: showTimeToBeDeleted.getLockedSeatIds())
			releaseLockOnSeatForShowTime(showTimeId, lockedSeatId);
		showTimeRepository.deleteById(showTimeId);
		return showTimeToBeDeleted;
	}
	
	
	
	//When seat is added initially, status is available and lockedByShowTimeIds are an empty set
	public Seat addSeat(Seat seat)
	{
		seat.setLockedByShowTimeIds(new HashSet<>());
		//seat.setSeatStatus(SeatStatus.AVAILABLE);
		Seat addedSeat = seatRepository.save(seat);
		addSeatToScreen(addedSeat);
		return addedSeat;
	}
	
	public void addSeatToScreen(Seat seat)
	{
		Screen parentScreen = getScreen(seat.getScreenId());
		Set<String> seatIds = parentScreen.getSeatIds();
		seatIds.add(seat.get_id());
		parentScreen.setSeatIds(seatIds);
		updateScreen(parentScreen.get_id(), parentScreen);
	}
	
	public Seat getSeat(String seatId)
	{
		Optional<Seat> foundSeat = seatRepository.findById(seatId);
		if(foundSeat.isPresent())
			return foundSeat.get();
		
		//custom exc
		return null;

	}
	
	public List<Seat> getAvailableSeatsForShowTime(String showTimeId)
	{
		List<Seat> availableSeats = new ArrayList<>();
		ShowTime showTime = getShowTime(showTimeId);
		Screen screen = getScreen(showTime.getScreenId());
		for(String seatId: screen.getSeatIds())
		{
			if(isSeatAvailableForShowTime(showTimeId, seatId))
				availableSeats.add(getSeat(seatId));
		}
		return availableSeats;
	}
	
	public Seat updateSeat(String seatId, Seat updatedSeat)
	{
		
		//first check if seat exists for this seatId
		updatedSeat.set_id(seatId);
		return seatRepository.save(updatedSeat);
	}
		
	
	//should delete Seat from all showtimes in screen as well
	public void deleteSeatFromScreen(Seat seat)
	{
		Screen parentScreen = getScreen(seat.getScreenId());
		Set<String> seatIds = parentScreen.getSeatIds();
		seatIds.remove(seat.get_id());
		parentScreen.setShowTimeIds(seatIds);
		updateScreen(parentScreen.get_id(), parentScreen);
	}
	
	
	public Seat deleteSeat(String seatId)
	{
		Seat seatToBeDeleted = getSeat(seatId);
		deleteSeatFromScreen(seatToBeDeleted);
		seatRepository.deleteById(seatId);
		return seatToBeDeleted;
	}
	
	public boolean isSeatAvailableForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		return !seat.getLockedByShowTimeIds().contains(showTimeId);
	}
	
	public void lockSeatForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		Set<String> lockedByShowTimeIds = seat.getLockedByShowTimeIds();
		lockedByShowTimeIds.add(showTimeId);
		seat.setLockedByShowTimeIds(lockedByShowTimeIds);
		updateSeat(seat.get_id(), seat);
		
	}
	
	public void lockSeatsForBooking(String bookingId) throws Exception
	{
		Booking booking = restTemplate.getForObject("http://booking-service/{bookingId}/bookings", Booking.class, bookingId);
		Set<String> lockedSeatIds = booking.getSeatIds();
		ShowTime bookingShowTime = getShowTime(booking.getShowTimeId());
		Set<String> lockedSeatIdsForBookingShowTime = bookingShowTime.getLockedSeatIds();
		for(String seatId: lockedSeatIds)
		{
			if(isSeatAvailableForShowTime(booking.getShowTimeId(), seatId))
				lockSeatForShowTime(booking.getShowTimeId(), seatId);
			else 
				throw new Exception();

		}
		lockedSeatIdsForBookingShowTime.addAll(lockedSeatIds);
		bookingShowTime.setLockedSeatIds(lockedSeatIdsForBookingShowTime);
		updateShowTime(booking.getShowTimeId(), bookingShowTime);
	}
	
	
	//incorrect function, only booking seats have to be locked for booking show time
	public void lockSeatsForShowTime(String showTimeId) throws Exception
	{
		for(String seatId: getShowTime(showTimeId).getLockedSeatIds())
		{
			if(isSeatAvailableForShowTime(showTimeId, seatId))
				lockSeatForShowTime(showTimeId, seatId);
			else 
				throw new Exception();
			//throw exception in else block, seat is not available
		}
	}
	
	public void releaseAllSeatsForShowTime(String showTimeId)
	{
		ShowTime showTime = getShowTime(showTimeId);
		Set<String> lockedSeatIds = showTime.getLockedSeatIds();
		for(String seatId: lockedSeatIds)
			releaseLockOnSeatForShowTime(showTimeId, seatId);
		lockedSeatIds.clear();
		showTime.setLockedSeatIds(lockedSeatIds);
		updateShowTime(showTimeId, showTime);
		
	}
	
	public void releaseSeatsForBooking(String bookingId)
	{
		Booking booking = restTemplate.getForObject("http://booking-service/{bookingId}/bookings", Booking.class, bookingId);
		ShowTime showTime = getShowTime(booking.getShowTimeId());
		Set<String> lockedSeatIdsForShowTime = showTime.getLockedSeatIds();
		Set<String> lockedSeatIdsForBooking = booking.getSeatIds();
		for(String seatId: lockedSeatIdsForBooking )
			releaseLockOnSeatForShowTime(booking.getShowTimeId(), seatId);
		lockedSeatIdsForShowTime.removeAll(lockedSeatIdsForBooking);
		showTime.setLockedSeatIds(lockedSeatIdsForShowTime);
		updateShowTime(booking.getShowTimeId(), showTime);

	}
	
	public void releaseLockOnSeatForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		Set<String> lockedByShowTimeIds = seat.getLockedByShowTimeIds();
		lockedByShowTimeIds.remove(showTimeId);
		seat.setLockedByShowTimeIds(lockedByShowTimeIds);
		updateSeat(seat.get_id(), seat);
	}
	
	

	

	
}
