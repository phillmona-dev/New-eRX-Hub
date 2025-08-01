package com.medco.eprescription_out_of_stock.Entitiy.Drug;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.medco.eprescription_out_of_stock.shared.enums.DrugStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drugs")
public class Drug implements Serializable {

    @Serial
    private static final long serialVersionUID = 4768448303484614360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String drugUuid = UUID.randomUUID().toString();

    @Size(max = 255)
    private String drugName;

    @Size(max = 30)
    private String drugCode;

    @Size(max = 100)
    private String category;

    private String subCategory;

    @Size(max = 100)
    private String strength;

    @Size(max = 255)
    private String volume;

    @Size(max = 50)
    private String drugUnit;

    @Size(max = 300)
    private String dosageForm;

    private String drugDescription;

    private String manufacturer;


    @Enumerated(EnumType.STRING)
    private DrugStatusEnum drugStatus;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted=false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drug_formulary_id", referencedColumnName = "id")
    @JsonManagedReference
    private DrugFormulary drugFormulary;


}
