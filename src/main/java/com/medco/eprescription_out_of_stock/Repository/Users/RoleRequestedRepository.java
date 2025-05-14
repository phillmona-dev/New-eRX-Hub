package com.medco.eprescription_out_of_stock.Repository.Users;
import com.medco.eprescription_out_of_stock.Entitiy.User.RequestedRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRequestedRepository  extends JpaRepository<RequestedRole,Long> {

    Optional<RequestedRole> findFirstByUserId(Long userId);


}
