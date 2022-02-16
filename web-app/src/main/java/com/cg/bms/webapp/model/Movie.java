package com.cg.bms.webapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
	
	private String name;
	private String genre;
	private double rating;
	private List<String> cast;


}
