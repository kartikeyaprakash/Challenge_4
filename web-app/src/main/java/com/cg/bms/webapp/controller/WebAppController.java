package com.cg.bms.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.webapp.model.Cities;
import com.cg.bms.webapp.model.City;
import com.cg.bms.webapp.model.LoginDTO;
import com.cg.bms.webapp.model.Theatre;
import com.cg.bms.webapp.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class WebAppController {
	
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@PostMapping("/register")
	public User register(@RequestBody User user)
	{
		return restTemplate.postForObject("http://user-service/users", user, User.class);
	}
	
	
	/**
	 * incorrect logic, need to study spring security, cant add Request Body in GET method
	 * @param loginDTO
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@GetMapping("/login")
	public User login(@RequestBody LoginDTO loginDTO) throws JsonMappingException, JsonProcessingException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
		body.add("email", loginDTO.getEmail());
		body.add("password", loginDTO.getPassword());

		HttpEntity<?> httpEntity = new HttpEntity<Object>(body , headers);
		ObjectMapper objectMapper = new ObjectMapper();
		return restTemplate.exchange("http://user-service/users/verify", HttpMethod.GET, httpEntity, User.class).getBody();
		//return objectMapper.readValue(response, User.class);
		
	}
	
	@GetMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String firstName, @RequestParam String userEmail) throws JsonMappingException, JsonProcessingException
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		//ObjectMapper objectMapper = new ObjectMapper();
		User foundUser = restTemplate.exchange("http://user-service/users/forgotPassword?firstName={firstName}&userEmail={userEmail}",HttpMethod.GET,httpEntity, User.class, firstName, userEmail).getBody();
		//User foundUser = objectMapper.readValue(response, User.class);
		if(foundUser!=null)
			return foundUser.getPassword();
		else
			return "";
	}
	
	@GetMapping("/{userId}/profile")
	public User displayProfile(@PathVariable("userId") String userId) 
	{
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		//ObjectMapper objectMapper = new ObjectMapper();
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		User foundUser = restTemplate.exchange("http://user-service/users/{userId}/profile",HttpMethod.GET,httpEntity,User.class,userId).getBody();
		//User foundUser = objectMapper.readValue(response, User.class);
		if(foundUser!=null)
			return foundUser;
		else
			return null;

	}
	
	@GetMapping("/index")
	public String index()
	{
		return "index";
	}
	
	
	
	/**
	 * add a city in the city Db
	 * @param userId
	 * @param city
	 * @return
	 */
	@PostMapping("/{userId}/cities")
	public City addCity(@PathVariable("userId") String userId, @RequestBody City city)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		User foundUser = restTemplate.exchange("http://user-service/users/{userId}/profile", HttpMethod.GET, httpEntity, User.class, userId).getBody();
		if(foundUser.getRole().equals("admin"))
		{
			return restTemplate.postForObject("http://city-service/cities", city, City.class);
		}

		//exception handling : user not admin
		return null;

	}
	
	
	
	/**
	 * Find all cities in app
	 * @return
	 */
	@GetMapping("/cities")
	public Cities getAllCities()
	{
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "*/*");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		return restTemplate.getForObject("http://city-service/cities", Cities.class);
		
		//return restTemplate.exchange("http://city-service/cities", HttpMethod.GET, httpEntity, Cities.class).getBody();

	}
	
	
	@GetMapping("/{cityId}/cities")
	public City getCity(@PathVariable("cityId") String cityId)
	{
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		return restTemplate.exchange("http://city-service/{cityId}/cities", HttpMethod.GET, httpEntity, City.class, cityId).getBody();

	}
	
	@PutMapping("{userId}/{cityId}/cities")
	public City updateCity(@PathVariable("userId") String userId, @PathVariable("cityId") String cityId, @RequestBody City updatedCity)
	{
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		User foundUser = restTemplate.exchange("http://user-service/users/{userId}/profile", HttpMethod.GET, httpEntity, User.class, userId).getBody();
		if(foundUser.getRole().equals("admin"))
		{
			restTemplate.put("http://city-service/{cityId}/cities", updatedCity, cityId);
			return restTemplate.getForObject("http://city-service/{cityId}/cities", City.class, cityId);
		}
		
		//exception
		return null;

	}
	
	@DeleteMapping("/{userId}/{cityId}/cities")
	public City deleteCity(@PathVariable("userId") String userId, @PathVariable("cityId") String cityId)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		User foundUser = restTemplate.exchange("http://user-service/users/{userId}/profile", HttpMethod.GET, httpEntity, User.class, userId).getBody();
		if(foundUser.getRole().equals("admin"))
		{
			return restTemplate.exchange("http://city-service/{cityId}/cities", HttpMethod.DELETE, httpEntity, City.class, cityId).getBody();
		}
		
		//custom exception
		return null;

	}
	
	
	
	
	@PostMapping("/{userId}/theatres")
	public Theatre addTheatre(@PathVariable("userId") String userId, @RequestBody Theatre theatre) 
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		
		HttpEntity<String> httpEntity = new HttpEntity<>(headers);

		User foundUser = restTemplate.exchange("http://user-service/users/{userId}", HttpMethod.GET, httpEntity, User.class, userId).getBody();
		if(foundUser.getRole().equals("admin"))
		{
			return restTemplate.postForObject("\"http://theatre-service/theatres", theatre, Theatre.class);
		}
		
		//exception handling : user not admin
		return null;
	}
	
	
	
}
