package com.cg.bms.bookingservice.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
	
	
	private String id;
	private String userName;
	private String movie;
	private String city;
	private String showTime; 
	private double cost;
	private String[] seatNumbers;

}
