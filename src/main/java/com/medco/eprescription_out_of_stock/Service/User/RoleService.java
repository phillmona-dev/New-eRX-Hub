package com.medco.eprescription_out_of_stock.Service.User;

import com.medco.eprescription_out_of_stock.Dto.Request.User.AddRolePrivilegesRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleUpdateRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleService {

    public RoleResponse createRole(RoleRequest roleRequest) throws BadRequestException;
    public ResponseEntity<?> updateRole(String roleUuid, @Valid RoleUpdateRequest roleUpdateRequest);
    public ResponseEntity<?> deleteRole(String roleString);
    public RoleResponse getRole(String roleUuid);
    public RoleMyResponse getRoles(int page, int limit);
    public ResponseEntity<?> addRolePrivileges(String roleUuid, @Valid AddRolePrivilegesRequest rolePrivilegesRequest);
    public ResponseEntity<?> deleteRolePrivileges(String roleUuid, @Valid AddRolePrivilegesRequest rolePrivilegesRequest);
    public List<RoleResponse> searchRoles(String searchKey, int page, int limit);
}
