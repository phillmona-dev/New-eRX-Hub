package com.medco.eprescription_out_of_stock.Entitiy.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.medco.eprescription_out_of_stock.shared.Audit;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_privileges")
public class RolePrivilege extends Audit {
    @Serial
    private static final long serialVersionUID = -4814904549349101427L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_uuid", referencedColumnName = "role_uuid", nullable = false)
    private Role role;

    // Correct mapping for privilege UUID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "privilege_uuid", referencedColumnName = "privilege_uuid", nullable = false)  // Ensure the correct column name
    private Privilege privilege;
}





