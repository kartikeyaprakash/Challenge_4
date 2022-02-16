package com.cg.bms.userservice.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.userservice.model.LoginDTO;
import com.cg.bms.userservice.model.User;
import com.cg.bms.userservice.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping
	public ResponseEntity<User> register(@RequestBody User user)
	{
		return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
	}
	
	@GetMapping("/verify")
	public ResponseEntity<User> verify(@RequestBody LoginDTO loginDTO)
	{
		if(userService.verify(loginDTO)!=null)
			return new ResponseEntity<>(userService.verify(loginDTO), HttpStatus.OK);
		else
		{
			return new ResponseEntity<>(userService.verify(loginDTO), HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/forgotPassword")
	public ResponseEntity<User> recoverPassword(@RequestParam String firstName, @RequestParam String userEmail)
	{
		if( userService.recoverPassword(firstName, userEmail)!=null)
			return new ResponseEntity<>(userService.recoverPassword(firstName, userEmail), HttpStatus.OK);
		else
			return new ResponseEntity<>(userService.recoverPassword(firstName, userEmail), HttpStatus.NOT_FOUND);

	}
	
	@GetMapping("/{userId}/profile")
	public ResponseEntity<User> getProfile(@PathVariable("userId") String userId)
	{
		User user = userService.getProfile(userId);
		if(user!=null)
			return new ResponseEntity<>(user, HttpStatus.OK);
		return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
	}
	

}
