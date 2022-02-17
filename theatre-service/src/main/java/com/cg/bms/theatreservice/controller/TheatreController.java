package com.cg.bms.theatreservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Seat;
import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.model.Theatres;
import com.cg.bms.theatreservice.service.TheatreService;

@RestController
public class TheatreController {
	
	@Autowired
	private TheatreService theatreService;
	
	
	@PostMapping("/theatres")
	public ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatre)
	{
		return new ResponseEntity<>(theatreService.addTheatre(theatre), HttpStatus.CREATED);
	}
	
	
	@GetMapping("/theatres")
	public ResponseEntity<Theatres> getAllTheatres()
	{
		return new ResponseEntity<>(theatreService.getAllTheatres(), HttpStatus.OK);
	}
	
	@GetMapping("/{theatreId}/theatres")
	public ResponseEntity<Theatre> getTheatre(@PathVariable("theatreId") String theatreId)
	{
		return new ResponseEntity<>(theatreService.getTheatre(theatreId), HttpStatus.OK);
	}
	
	@GetMapping("cities/{cityId}/theatres")
	public  ResponseEntity<Theatres> getTheatresByCity(@PathVariable("cityId") String cityId)
	{
		return new ResponseEntity<>(theatreService.getTheatresByCity(cityId), HttpStatus.OK);
	}
	
	
	@PutMapping("/{theatreId}/theatres")
	public ResponseEntity<Theatre> updateTheatre(@PathVariable("theatreId") String theatreId, @RequestBody Theatre updatedTheatre)
	{
		return new ResponseEntity<>(theatreService.updateTheatre(theatreId, updatedTheatre), HttpStatus.OK);
	}
	
	@DeleteMapping("/{theatreId}/theatres")
	public ResponseEntity<Theatre> deleteTheatre(@PathVariable("theatreId") String theatreId)
	{
		return new ResponseEntity<>(theatreService.deleteTheatre(theatreId), HttpStatus.OK);
	}
	
	@PostMapping("/screens")
	public ResponseEntity<Screen> addScreen(@RequestBody Screen screen)
	{
		return new ResponseEntity<>(theatreService.addScreen(screen), HttpStatus.CREATED);
	}
	
	@GetMapping("/{theatreId}/screens")
	public  ResponseEntity<List<Screen>> getScreensForTheatre(@PathVariable("theatreId") String theatreId) 
	{
		return new ResponseEntity<>(theatreService.getScreensForTheatre(theatreId), HttpStatus.OK);
	}
	
	@PutMapping("/{screenId}/screens")
	public ResponseEntity<Screen> updateScreen(@PathVariable("screenId") String screenId, @RequestBody Screen updatedScreen)
	{
		return new ResponseEntity<>(theatreService.updateScreen(screenId, updatedScreen), HttpStatus.OK);
	}
	
	@DeleteMapping("/{screenId}/screens")
	public ResponseEntity<Screen> deleteScreen(@PathVariable("screenId") String screenId)
	{
		return new ResponseEntity<>(theatreService.deleteScreen(screenId), HttpStatus.OK);
	}
	
	
	@PostMapping("/showTimes")
	public ResponseEntity<ShowTime> addShowTime(@RequestBody ShowTime showTime)
	{
		return new ResponseEntity<>(theatreService.addShowTime(showTime), HttpStatus.CREATED);
	}
	
	
	@GetMapping("{screenId}/showTimes")
	public  ResponseEntity<List<ShowTime>> getShowTimesForScreen(@PathVariable("screenId") String screenId)
	{
		return new ResponseEntity<>(theatreService.getShowTimesForScreen(screenId), HttpStatus.OK);
	}
	
	@PutMapping("/{showTimeId}/showTimes")
	public ResponseEntity<ShowTime> updateShowTime(@PathVariable("showTimeId") String showTimeId, @RequestBody ShowTime updatedShowTime)
	{
		return new ResponseEntity<>(theatreService.updateShowTime(showTimeId, updatedShowTime), HttpStatus.OK);
	}
	
	@DeleteMapping("/{showTimeId}/showTimes")
	public ResponseEntity<ShowTime> deleteShowTime(@PathVariable("showTimeId") String showTimeId)
	{
		return new ResponseEntity<>(theatreService.deleteShowTime(showTimeId), HttpStatus.OK);
	}
	
	
	@PostMapping("/seats")
	public ResponseEntity<Seat> addSeat(@RequestBody Seat seat)
	{
		return new ResponseEntity<>(theatreService.addSeat(seat), HttpStatus.CREATED);
	}
	
	@GetMapping("{showTimeId}/seats")
	public  ResponseEntity<List<Seat>> getSeatsForShowTime(@PathVariable("showTimeId") String showTimeId, @RequestParam Optional<String> action)
	{
		if(!action.isPresent())
			return new ResponseEntity<>(theatreService.getAvailableSeatsForShowTime(showTimeId), HttpStatus.OK);
		else if(action.get().equals("lock"))
			theatreService.lockSeatsForShowTime(showTimeId);
		
		
		//think about this
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PutMapping("/{seatId}/seats")
	public ResponseEntity<Seat> updateSeat(@PathVariable("seatId") String seatId, @RequestBody Seat updatedSeat)
	{
		return new ResponseEntity<>(theatreService.updateSeat(seatId, updatedSeat), HttpStatus.OK);
	}

	@DeleteMapping("/{seatId}/seats")
	public ResponseEntity<Seat> deleteSeat(@PathVariable("seatId") String seatId)
	{
		return new ResponseEntity<>(theatreService.deleteSeat(seatId), HttpStatus.OK);
	}
	
	


	
	

}
