package com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Drug;
import com.medco.eprescription_out_of_stock.shared.enums.DrugStatusEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrugDto {

    private String drugUuid;

    @Size(max = 255)
    @NotNull
    private String drugName;

    @Size(max = 30)
    private String drugCode;

    @Size(max = 100)
    private String category;

    @Size(max = 110)
    private String subCategory;

    @Size(max = 110)
    private String strength;

    @Size(max = 110)
    private String volume;

    @Size(max = 100)
    private String drugUnit;

    private String drugDescription;

    @Size(max = 300)
    private String dosageForm;

    @NotNull
    private DrugStatusEnum drugStatus;

    private boolean isDeleted;

    private DrugInfoDto drugInfo;
}
