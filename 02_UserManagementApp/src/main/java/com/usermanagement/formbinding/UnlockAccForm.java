package com.usermanagement.formbinding;

import lombok.Data;

@Data
public class UnlockAccForm {

	private String userEmail;
	private String temPassword;
	private String newPassword;
	private String confurmPassword; 
}
