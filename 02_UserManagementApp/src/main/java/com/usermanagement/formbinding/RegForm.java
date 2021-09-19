package com.usermanagement.formbinding;

import java.time.LocalDate;

import lombok.Data;
@Data
public class RegForm {

	private String firstName;
	private String lastName;
	private String userEmail;
	private String userMobile;
	private LocalDate dob;
	private String gender;
	private Integer countryId;
	private Integer stateId;
	private Integer cityId;
	
}
