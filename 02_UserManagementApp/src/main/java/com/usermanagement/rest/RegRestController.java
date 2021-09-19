package com.usermanagement.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.constants.AppConstants;
import com.usermanagement.exception.UserManagementAppException;
import com.usermanagement.formbinding.RegForm;
import com.usermanagement.properties.AppProperties;
import com.usermanagement.service.UserService;

@RestController
public class RegRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private AppProperties apProp;

	@GetMapping(value = "/countries")
	private Map<Integer, String> getCountry() {
		return userService.getCountry();
	}
	
	@GetMapping(value = "/states/{countryId}")
	private Map<Integer, String> getStates(@PathVariable Integer countryId){
		return userService.getState(countryId);
	}
	
	@GetMapping(value = "/cities/{stateId}")
	private Map<Integer,String> getCity(@PathVariable Integer stateId){
		return userService.getCity(stateId);
	}
	
	@GetMapping(value = "/checkemail/{email}")
	public  ResponseEntity<String> uniqueEmailCheck(@PathVariable String email) {
		String checkEmail = userService.checkEmail(email);
		return new ResponseEntity<>(checkEmail,HttpStatus.OK);
	}
	
	@PostMapping(value = "/saveuser")
	public ResponseEntity<String> saveUser(@RequestBody RegForm regForm) throws UserManagementAppException {
		
		boolean saveUser = userService.hndlSignUpBtn(regForm);
		if(saveUser) {
			return new ResponseEntity<>(apProp.getMessages().get(AppConstants.REG_SUCCESS),HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(apProp.getMessages().get(AppConstants.REG_FAILED),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
