package com.cg.bms.theatreservice.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "screen")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Screen {
	
	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	private Set<String> showTimeIds;
	private String theatreId;
	private Set<String> seatIds;


}
