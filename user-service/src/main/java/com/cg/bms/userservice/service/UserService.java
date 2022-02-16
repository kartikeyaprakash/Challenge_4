package com.cg.bms.userservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.bms.userservice.model.LoginDTO;
import com.cg.bms.userservice.model.User;
import com.cg.bms.userservice.repository.UserRepository;

@Transactional
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User register(User user)
	{
		return userRepository.save(user);
	}
	
	public User verify(LoginDTO loginDTO)
	{
		User user = userRepository.findByEmail(loginDTO.getEmail());
		if(loginDTO.getPassword().equals(user.getPassword()))
			return user;
		else
			return null;
	}
	
	public User recoverPassword(String firstName, String userEmail)
	{
		User user = userRepository.findByFirstName(firstName);
		if(user.getEmail().equals(userEmail))
			return user;
		else
			return null;
	}
	
	public User getProfile(String userId)
	{
		//return userRepository.findByFirstName(userId);
		
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent())
			return user.get();
		
		//custom exceptions
		return null;
			
	}

}
