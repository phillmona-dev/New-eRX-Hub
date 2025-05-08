package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PrivilegeMyResponse {
    private long totalPages;
    private List<PrivilegeResponse> response;
}
