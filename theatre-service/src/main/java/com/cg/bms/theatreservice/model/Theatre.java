package com.cg.bms.theatreservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
	
	@Id
	private String id;
	
	private String name;
	private List<String> movies;
	private List<String> screens;
	private String city;
	

}
