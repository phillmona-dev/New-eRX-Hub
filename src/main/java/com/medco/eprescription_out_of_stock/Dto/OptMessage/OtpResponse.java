package com.medco.eprescription_out_of_stock.Dto.OptMessage;

public class OtpResponse {

    private boolean success;
    private String message;


    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }


}
