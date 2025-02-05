package com.api.handler;

import com.api.handler.custom_exception.CustomNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.OperationsException;
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
                .code(StatusCode.INVALID_ARGUMENT)
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
                .code(StatusCode.INTERNAL_SERVER_ERROR)
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
                .code(StatusCode.INVALID_ARGUMENT)
                .message("Wrong username or password")
                .build();
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result notFoundException(CustomNotFoundException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(OperationsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result operationsException(OperationsException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.INVALID_ARGUMENT)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(PermissionDeniedDataAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result permissionDeniedDataAccessException(PermissionDeniedDataAccessException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.UNAUTHORIZED)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result expiredJwtException(ExpiredJwtException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.INVALID_ARGUMENT)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result malformedJwtException(MalformedJwtException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.INVALID_ARGUMENT)
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeException(RuntimeException ex){
        return Result
                .builder()
                .flag(false)
                .code(StatusCode.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();
    }
}
