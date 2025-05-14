package com.medco.eprescription_out_of_stock.Entitiy.User;
import com.medco.eprescription_out_of_stock.shared.enums.HealthCenterType;
import com.medco.eprescription_out_of_stock.shared.enums.OwnershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HealthCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private HealthCenterType healthCenterType;

    private String address;

    private String mobilePhone;

    private String email;

//    private String username;

//    private String password;

    @Enumerated(EnumType.STRING)
    private OwnershipType ownershipType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;



//    @OneToMany(mappedBy = "healthCenter", cascade = CascadeType.ALL)
//    private List<EvidenceDocument> documents;

}
