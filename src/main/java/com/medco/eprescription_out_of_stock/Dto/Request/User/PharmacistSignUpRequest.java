package com.medco.eprescription_out_of_stock.Dto.Request.User;
import com.medco.eprescription_out_of_stock.shared.enums.PharmacyPersonnelType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PharmacistSignUpRequest extends UserRequest {

        private String pharmacyUuid;
        private String pharmacyName;
        private String educationLevel;
        private String graduatedFrom;
        private String graduationYear;
        private String licenceNo;
        private String licenceExpirationDate;
        private String qualificationLevel;
        private PharmacyPersonnelType pharmacyPersonnelType;
        private List<MultipartFile> files;

}
