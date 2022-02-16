package com.cg.bms.webapp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theatre {
	
	
	@JsonIgnore
	private String _id;
	private String name;
	//private List<Screen> screens;
	private String cityId;
	

}