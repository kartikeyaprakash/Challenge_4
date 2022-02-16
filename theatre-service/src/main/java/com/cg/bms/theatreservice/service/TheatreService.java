package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.model.City;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.model.Theatres;
import com.cg.bms.theatreservice.repository.TheatreRepository;

@Service
@Transactional
public class TheatreService {
	
	@Autowired
	private TheatreRepository theatreRepository;
	
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
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		return restTemplate.exchange("http://city-service/{cityId}/cities", HttpMethod.GET, httpEntity, City.class, cityId).getBody();
	}
	
	
	//Update Theatre DTO to be different, will not include City ID
	public Theatre updateTheatre(String theatreId, Theatre updatedTheatre)
	{
		Theatre foundTheatre = getTheatre(theatreId);
		foundTheatre.setCityId(updatedTheatre.getCityId());
		foundTheatre.setName(updatedTheatre.getName());
		foundTheatre.setNumberOfScreens(updatedTheatre.getNumberOfScreens());
		return theatreRepository.save(foundTheatre);
	}
	
	public Theatre deleteTheatre(String theatreId)
	{
		Theatre theatreToBeDeleted = getTheatre(theatreId);
		removeTheatreFromCity(theatreId);
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

	
	

	
}
