package com.medco.eprescription_out_of_stock.Dto.Request.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String roleName;

    @NotBlank
    @Size(max = 100)
    private String roleDescription;

    private String [] privileges;

}
