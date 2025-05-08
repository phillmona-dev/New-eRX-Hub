package com.medco.eprescription_out_of_stock.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInvalidEmailException(EmailAlreadyExists ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInvalidPhone(InvalidPhoneException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleSubCityNameAlreadyExistsException(SubCityNameAlreadyExistsException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }











    @ExceptionHandler
    public  ResponseEntity<ErrorResponse> handleNetworkErrorException(NetworkErrorException ex)
    {
        ErrorResponse message = new ErrorResponse(
                HttpStatus.REQUEST_TIMEOUT.value(),
                new Date(System.currentTimeMillis()),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(message);


    }









}
