package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleMyResponse {

    private long totalPages;
    private List<RoleResponse> response;
}
