package com.cg.bms.theatreservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
