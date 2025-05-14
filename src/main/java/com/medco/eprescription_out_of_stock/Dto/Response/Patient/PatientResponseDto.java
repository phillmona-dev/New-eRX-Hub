package com.medco.eprescription_out_of_stock.Dto.Response.Patient;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PatientResponseDto {

    private Long totalPage;
    private List<PatientResponse> response;

}
