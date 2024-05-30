package com.ispan.theater.exception;

import java.time.LocalDateTime;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ispan.theater.dto.ErrorDto;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorDto> handleException(Exception e){
		return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(new ErrorDto(LocalDateTime.now(),HttpStatus.SC_FORBIDDEN,"forbidden",e.getMessage()));
	}
	
}
