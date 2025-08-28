package com.medco.eprescription_out_of_stock.Entitiy.User;
import com.medco.eprescription_out_of_stock.shared.Audit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "privileges", uniqueConstraints = {
        @UniqueConstraint(columnNames = "privilegeName"),
        @UniqueConstraint(columnNames = "privilege_uuid")
})
public class Privilege extends Audit {

    @Serial
    private static final long serialVersionUID = 2369844719759914085L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "privilege_uuid", unique = true, nullable = false)
    private String privilegeUuid = UUID.randomUUID().toString();

    @Column(length = 50, nullable = false)
    private String privilegeName;

    @NotBlank
    @Size(max = 100)
    private String privilegeDescription;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String privilegeCategory;

    private String privilegeType;

    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolePrivilege> rolePrivileges = new ArrayList<>();
}
