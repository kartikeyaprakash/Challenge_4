package com.cg.bms.webapp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
	
	@JsonIgnore
	private String id;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String role;
	private String address;
	
	
	
	

}
