package com.cg.bms.cityservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.bms.cityservice.model.Cities;
import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.repository.CityRepository;


@Service
@Transactional
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	
	public City addCity(City city)
	{
		return cityRepository.save(city);
	}
	
	public Cities getAllCities()
	{
		List<City> cities = cityRepository.findAll();
		return Cities.builder().cities(cities).build();
	}
	
	public City getCity(String cityId)
	{
		Optional<City> city = cityRepository.findById(cityId);
		if(city.isPresent())
			return city.get();
		
		
		//add custom exception
		return null;
	}
	
	public City updateCity(String cityId, City updatedCity)
	{
		City foundCity = getCity(cityId);
		foundCity.setName(updatedCity.getName());
		foundCity.setState(updatedCity.getState());
		foundCity.setTheatreIds(updatedCity.getTheatreIds());
		return cityRepository.save(foundCity);
	}
	
	public void deleteCity(String cityId)
	{
		cityRepository.deleteById(cityId);
	}

}
