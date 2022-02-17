package com.cg.bms.theatreservice.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(value = "showtime")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowTime {
	
	@Id
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date endTime;
	private String screenId;
	private Set<String> lockedSeatIds;

}
