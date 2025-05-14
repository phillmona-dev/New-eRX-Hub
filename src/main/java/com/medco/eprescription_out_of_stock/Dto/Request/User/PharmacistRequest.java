package com.medco.eprescription_out_of_stock.Dto.Request.User;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PharmacistRequest extends UserRequest{

    private String educationLevel;
    private String graduatedFrom;
    private String graduationYear;
    private String licenceNo;
    private String licenceExpirationDate;
    private String qualificationLevel;

}
