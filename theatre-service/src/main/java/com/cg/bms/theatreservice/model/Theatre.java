package com.cg.bms.theatreservice.model;


import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Id
	private String _id;
	
	
	private String name;
	private Set<String> screenIds;
	private String cityId;
	
}

