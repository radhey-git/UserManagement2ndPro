package com.usermanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.exception.UserManagementAppException;
import com.usermanagement.formbinding.LoginForm;
import com.usermanagement.service.UserService;

@RestController
@RequestMapping("/loginController")
public class LoginRestController {

	@Autowired
	UserService userServer;

	@PostMapping("/login")
	public String loginFun(@RequestBody LoginForm loginForm) throws UserManagementAppException {
		return userServer.handleSignInBtn(loginForm);
	}
}
