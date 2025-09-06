package com.medco.eprescription_out_of_stock.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(true));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DrugNotFoundException.class)
    public ResponseEntity<ErrorMessage> BadRequestExceptionHandler(DrugNotFoundException ex,WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(true));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler( PrescriptionNotFoundException.class)
    public ResponseEntity<ErrorMessage> BadRequestExceptionHandler( PrescriptionNotFoundException ex,WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(true));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<ErrorMessage> customApplicationExceptionHandler(CustomApplicationException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatus().value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, ex.getStatus());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();

        if (errorMessage != null && errorMessage.contains("Transaction silently rolled back")) {
            Throwable cause = ex.getCause();
            if (cause != null && cause.getCause() != null) {
                errorMessage = cause.getCause().getMessage();
            } else if (cause != null) {
                errorMessage = cause.getMessage();
            }
        }

        if (errorMessage != null && errorMessage.contains("Transaction silently rolled back")) {
            errorMessage = "An error occurred while processing your request. Please try again later.";
        }

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                errorMessage,
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
