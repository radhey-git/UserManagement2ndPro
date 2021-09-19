package com.usermanagement.exception;

import java.time.LocalDateTime;
import lombok.Data;
@Data
public class ErrorResponse {
	private String errorCode;
	private String errorMsg;
	private LocalDateTime dateTime;
}
