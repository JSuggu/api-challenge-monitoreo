package com.api.handler;

import com.api.handler.custom_exception.CustomNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.OperationsException;
import java.util.*;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            description = "Method argument not valid."
    )
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getClass().getSimpleName());

        for(FieldError error : ex.getFieldErrors()){
            errorDetails.put(error.getField().toLowerCase(), error.getDefaultMessage());
        }

        return ResponseEntity.status(StatusCode.INVALID_ARGUMENT).body(errorDetails);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            description = "Method argument not valid."
    )
    public ResponseEntity<Map<String, String>> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INVALID_ARGUMENT).body(errorDetails);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
            responseCode = "500",
            description = "Data integrity violation."
    )
    public ResponseEntity<Map<String, String>> dataIntegrityViolationException(DataIntegrityViolationException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(
            responseCode = "401",
            description = "Bad credentials."
    )
    public ResponseEntity<Map<String, String>> badCredentialsException(BadCredentialsException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", "Wrong username or password");

        return ResponseEntity.status(StatusCode.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
            responseCode = "404",
            description = "Not found."
    )
    public ResponseEntity<Map<String, String>> notFoundException(CustomNotFoundException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(OperationsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            description = "Failed operation."
    )
    public ResponseEntity<Map<String, String>> operationsException(OperationsException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INVALID_ARGUMENT).body(errorDetails);
    }

    @ExceptionHandler(PermissionDeniedDataAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(
            responseCode = "401",
            description = "Data integrity violation."
    )
    public ResponseEntity<Map<String, String>> permissionDeniedDataAccessException(PermissionDeniedDataAccessException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.UNAUTHORIZED).body(errorDetails);
    }

    @ApiResponse(
            responseCode = "400",
            description = "Expired token."
    )
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> expiredJwtException(ExpiredJwtException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INVALID_ARGUMENT).body(errorDetails);
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            description = "Malformed token."
    )
    public ResponseEntity<Map<String, String>> malformedJwtException(MalformedJwtException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INVALID_ARGUMENT).body(errorDetails);
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(
            responseCode = "500",
            description = "Internal server error."
    )
    public ResponseEntity<Map<String, String>> runtimeException(RuntimeException ex){
        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("error", ex.getClass().getSimpleName());
        errorDetails.put("message", ex.getMessage());

        return ResponseEntity.status(StatusCode.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}
