package com.medco.eprescription_out_of_stock.Dto.Request.User;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadProfileRequest {
    private MultipartFile profilePicture;
    private String userUuid;
}
