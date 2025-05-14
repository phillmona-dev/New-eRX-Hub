package com.medco.eprescription_out_of_stock.Exception;

public class DrugNotFoundException extends RuntimeException{
    public DrugNotFoundException(String msg) {
        super(msg);
    }
}
