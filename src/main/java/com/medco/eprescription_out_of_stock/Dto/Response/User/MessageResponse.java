package com.medco.eprescription_out_of_stock.Dto.Response.User;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {

    private String message;

    public MessageResponse(String message) {

        this.message = message;
    }

}
