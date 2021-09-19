package com.usermanagement.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionMapper {
	@ExceptionHandler(value = SMTPException.class)
	public ResponseEntity<ErrorResponse> handlingSmtpException(SMTPException exception){
		ErrorResponse response=new ErrorResponse();
		response.setErrorCode("ERR102");
		response.setErrorMsg(exception.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(value = FileMissingException.class)
	public ResponseEntity<ErrorResponse> handleFileMissingException(FileMissingException excep){
		ErrorResponse response=new ErrorResponse();
		response.setErrorCode("ERR103");
		response.setErrorMsg(excep.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(value = PasswordEncryptDecryptException.class)
	public ResponseEntity<ErrorResponse> handlePwdEncDecException(PasswordEncryptDecryptException exception){
		ErrorResponse response=new ErrorResponse();
		response.setErrorCode("ERR104");
		response.setErrorMsg(exception.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(value = UserManagementAppException.class)
	public ResponseEntity<ErrorResponse> handlePwdEncDecException(UserManagementAppException exception){
		ErrorResponse response=new ErrorResponse();
		response.setErrorCode("UAE101");
		response.setErrorMsg(exception.getMessage());
		response.setDateTime(LocalDateTime.now());
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
