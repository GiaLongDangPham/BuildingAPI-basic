package com.javaweb.controllerAdvice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javaweb.model.ErrorResponeDTO;

import customExceptions.FieldRequiredException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<Object> handleArithmeticException(ArithmeticException ex, WebRequest request) {

		ErrorResponeDTO err = new ErrorResponeDTO();
		err.setError(ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("chia cho 0 rồi");
		err.setDetails(details);
		
	    return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(FieldRequiredException.class)
	public ResponseEntity<Object> handleFieldRequiredException(FieldRequiredException ex, WebRequest request) {
		
		ErrorResponeDTO err = new ErrorResponeDTO();
		err.setError(ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("check name or number again pls");
		err.setDetails(details);
		
	    return new ResponseEntity<>(err, HttpStatus.BAD_GATEWAY);
	}
	

}
