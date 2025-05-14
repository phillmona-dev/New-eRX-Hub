package com.medco.eprescription_out_of_stock.Exception;

public class PrescriptionNotFoundException extends RuntimeException{
    public PrescriptionNotFoundException(String msg)
    {
        super(msg);
    }

}
