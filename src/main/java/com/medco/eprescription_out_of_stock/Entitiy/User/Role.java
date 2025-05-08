package com.medco.eprescription_out_of_stock.Entitiy.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.medco.eprescription_out_of_stock.shared.Audit;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "roleName"),
        @UniqueConstraint(columnNames = "role_uuid")  // Ensure the column name matches the database
})
public class Role extends Audit {
    @Serial
    private static final long serialVersionUID = 4768448303484614360L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 50)
    private String roleName;

    @NotBlank
    @Size(max = 100)
    private String roleDescription;

    @Column(name = "role_uuid", unique = true, nullable = false)
    private String roleUuid = UUID.randomUUID().toString();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_privileges",
            joinColumns = @JoinColumn(name = "role_uuid", referencedColumnName = "role_uuid"),
            inverseJoinColumns = @JoinColumn(name = "privilege_uuid", referencedColumnName = "privilege_uuid"))
    private Set<Privilege> privileges = new HashSet<>();

    public Role(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }
}


