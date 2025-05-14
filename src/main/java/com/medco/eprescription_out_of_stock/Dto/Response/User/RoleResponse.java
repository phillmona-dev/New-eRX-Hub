package com.medco.eprescription_out_of_stock.Dto.Response.User;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleResponse {
    private String roleName;
    private String roleDescription;
    private String roleUuid;
    private List<String> privileges;
}