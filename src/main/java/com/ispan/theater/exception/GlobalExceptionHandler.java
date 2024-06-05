package com.ispan.theater.exception;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ispan.theater.dto.ErrorDto;
import com.ispan.theater.util.DatetimeConverter;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleException(Exception e){
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()),HttpStatus.SC_INTERNAL_SERVER_ERROR,"INTERNAL",e.getMessage()));
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorDto> handleJwtException(ExpiredJwtException e){
		return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()),HttpStatus.SC_FORBIDDEN,"forbidden",e.getMessage()));
	}
	
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<ErrorDto> handleOrderException(OrderException e){
		return ResponseEntity.status(e.getErrorCode()).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()),e.getErrorCode(),"order_ticket_error",e.getErrorMessage()));
	}

}
