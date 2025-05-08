package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserMyResponse {
    private long totalPages;
    private long totalElements;
    private int pageNumber;
    private List<UserResponse> response;
}
