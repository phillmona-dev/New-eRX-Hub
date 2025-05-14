package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Drug;
import com.medco.eprescription_out_of_stock.shared.enums.DrugBrandsStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugBrandsDto {
    private Long id;
    private String drugBrandUuid;
    private String drugBrandCode;
    private String drugBrandName;
    private DrugBrandsStatusEnum drugBrandsStatus;
    private boolean isDeleted;
    private Long drugId; // Assuming Drug entity has an ID of type Long

    public static class DrugRequestDto {
    }
}

