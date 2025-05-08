package com.medco.eprescription_out_of_stock.Exception;

public class NetworkErrorException extends RuntimeException{

   public  NetworkErrorException(String message)
   {
       super(message);
   }

}
