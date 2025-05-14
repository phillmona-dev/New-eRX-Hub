package com.medco.eprescription_out_of_stock.Dto.Response.Pharmacy;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PharmacyResponseDto {

    private Long totalPage;
    private List<PharmacyResponse> response;
}
