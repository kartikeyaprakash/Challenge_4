package com.cg.bms.theatreservice.model;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "theatre")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theatre {
	
	
	@JsonIgnore
	@Id
	private String _id;
	
	
	private String name;
	private Integer numberOfScreens;
	//private List<Screen> screens;
	private String cityId;
	
}

