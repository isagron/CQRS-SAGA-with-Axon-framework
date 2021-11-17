package com.isagron.estore.ProductService.api.handlers;

import com.isagron.estore.ProductService.api.dto.ErrorDto;
import org.axonframework.commandhandling.CommandExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<ErrorDto> handleIllegalStateException(IllegalStateException ex, WebRequest webRequest){
        return new ResponseEntity<>(
               ErrorDto.builder().timestamp(new Date()).message(ex.getMessage()).build(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CommandExecutionException.class})
    public ResponseEntity<ErrorDto> handleCommandExecutionException(CommandExecutionException ex, WebRequest webRequest){
        return new ResponseEntity<>(
                ErrorDto.builder().timestamp(new Date()).message(ex.getMessage()).build(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorDto> handleAllOtherExceptions(Exception ex, WebRequest webRequest){
        return new ResponseEntity<>(
                ErrorDto.builder().timestamp(new Date()).message(ex.getMessage()).build(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
