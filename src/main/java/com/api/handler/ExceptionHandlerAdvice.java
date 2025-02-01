package com.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errorDetails = new HashMap<>();

        for(FieldError error : ex.getFieldErrors()){
            errorDetails.put(error.getField(), error.getDefaultMessage());
        }

        return Result
                .builder()
                .flag(false)
                .code(400)
                .message(ex.getBody().getDetail())
                .data(errorDetails)
                .build();
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result sqlException(SQLException ex){
        String[] error = ex.getMessage().split("\n");
        String[] message = error[0].trim().split(":");
        String[] detail = error[1].trim().split(":");

        Map<String, String> errorDetails = new HashMap<>();


        errorDetails.put(message[0], message[1]);
        errorDetails.put(detail[0], detail[1]);

        return Result
                .builder()
                .flag(false)
                .code(500)
                .message("Database error")
                .data(errorDetails)
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result badCredentialsException(BadCredentialsException ex){

        return Result
                .builder()
                .flag(false)
                .code(401)
                .message("Wrong username or password")
                .build();
    }
}
