package com.medco.eprescription_out_of_stock.Exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(){

        super("No Account Found With The Credentials Provided!");
    }
}
