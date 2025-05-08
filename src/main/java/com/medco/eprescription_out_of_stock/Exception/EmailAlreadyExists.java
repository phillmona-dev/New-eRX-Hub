package com.medco.eprescription_out_of_stock.Exception;

public class EmailAlreadyExists extends RuntimeException{

    public EmailAlreadyExists(String message){
        super(message);
    }
}
