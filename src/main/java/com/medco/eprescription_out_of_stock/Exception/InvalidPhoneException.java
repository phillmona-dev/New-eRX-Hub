package com.medco.eprescription_out_of_stock.Exception;

public class InvalidPhoneException extends RuntimeException{
    public InvalidPhoneException(String message) {
        super(message);
    }
}
