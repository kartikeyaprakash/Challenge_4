package com.cg.bms.theatreservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.repository.TheatreRepository;

@RestController
@RequestMapping("/api/theatres")
public class TheatreController {
	
	
	@Autowired
	private TheatreRepository theatreRepository;
	
	
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Theatre> findAll() {
        return theatreRepository.findAll();
    }

    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody Theatre theatre) {
    	theatreRepository.insert(theatre);
    }
    
    
    
}
