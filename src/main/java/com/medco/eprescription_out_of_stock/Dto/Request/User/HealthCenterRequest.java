package com.medco.eprescription_out_of_stock.Dto.Request.User;
import com.medco.eprescription_out_of_stock.shared.enums.HealthCenterType;
import com.medco.eprescription_out_of_stock.shared.enums.OwnershipType;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HealthCenterRequest {

    private String email;
    private String password;
    private String name;
    private String address;
    private String mobilePhone;
//    private String username;
    private HealthCenterType type;
    private OwnershipType ownershipType;
    private String roleUuid;
    private UserStatus userStatus;

}
