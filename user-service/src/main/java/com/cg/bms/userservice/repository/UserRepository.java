package com.cg.bms.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.userservice.model.User;


@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	public User findByEmail(String email);
	public User findByFirstName(String firstName);


}
