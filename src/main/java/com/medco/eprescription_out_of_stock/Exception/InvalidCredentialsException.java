package com.medco.eprescription_out_of_stock.Exception;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(){
        super("Invalid UserName or Password");
    }
}
