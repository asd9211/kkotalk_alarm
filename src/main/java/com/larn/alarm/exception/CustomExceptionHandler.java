package com.larn.alarm.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(ServiceException.class)
	public String custom(HttpServletRequest request, ServiceException e) {
		return e.getMessage();
	}
}
