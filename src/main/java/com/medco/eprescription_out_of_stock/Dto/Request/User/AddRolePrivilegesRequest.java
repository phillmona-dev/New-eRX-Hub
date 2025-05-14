package com.medco.eprescription_out_of_stock.Dto.Request.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddRolePrivilegesRequest {

    private String[] privileges;
}
