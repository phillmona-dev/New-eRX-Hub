package com.medco.eprescription_out_of_stock.Dto.Response.User;

import com.medco.eprescription_out_of_stock.shared.enums.HealthCenterType;
import com.medco.eprescription_out_of_stock.shared.enums.OwnershipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthCenterResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String mobilePhone;
    private HealthCenterType healthCenterType;
    private OwnershipType ownershipType;
}

