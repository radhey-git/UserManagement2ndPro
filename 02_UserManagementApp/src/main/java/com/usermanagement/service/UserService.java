package com.usermanagement.service;

import java.util.Map;

import com.usermanagement.exception.UserManagementAppException;
import com.usermanagement.formbinding.LoginForm;
import com.usermanagement.formbinding.RegForm;
import com.usermanagement.formbinding.UnlockAccForm;

public interface UserService {

	
	public Map<Integer, String> getCountry();
	
	public Map<Integer, String> getState(Integer countryId);
	
	public Map<Integer, String> getCity(Integer stateId);
	
	public String handleSignInBtn(LoginForm loginForm) throws UserManagementAppException; //user is lock or unlock or check credential due to 
														//all these checking need String return type
	
	public String checkEmail(String email); //valid email checking
	
	public boolean hndlSignUpBtn(RegForm regForm) throws UserManagementAppException; //save New User
	
	public boolean unlckAccFnctnlty(UnlockAccForm unloclAccForm) throws UserManagementAppException;
	
	public boolean frgtPwdFuctnlty(String email) throws UserManagementAppException;
	
}
