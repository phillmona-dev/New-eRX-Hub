package com.medco.eprescription_out_of_stock.Controller.User;
import com.medco.eprescription_out_of_stock.Dto.Request.User.AddRolePrivilegesRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.RoleUpdateRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.RoleResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Service.User.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/erx/e-prescription/users/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {

        this.roleService = roleService;
    }

    @PostMapping
    public RoleResponse createRole(@Valid @RequestBody RoleRequest roleRequest) throws BadRequestException {
        return roleService.createRole(roleRequest);
    }

    @PutMapping(path="/{roleUuid}")
    //    @PreAuthorize("hasRole('UPDATE_ROLE')")
    public ResponseEntity<?> updateRole(@PathVariable String roleUuid, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return roleService.updateRole(roleUuid, roleUpdateRequest);
    }

    @PutMapping("privileges/{roleUuid}")
    //    @PreAuthorize("hasRole('Update-Role')")
    public ResponseEntity<?> addRolePrivileges(@PathVariable String roleUuid, @Valid @RequestBody AddRolePrivilegesRequest rolePrivilegesRequest) {
        return roleService.addRolePrivileges(roleUuid, rolePrivilegesRequest);
    }

    @GetMapping(path="/{roleUuid}")
    // @PreAuthorize("hasRole('Read-Role')")
    public RoleResponse getRole(@PathVariable String roleUuid) {
        return roleService.getRole(roleUuid);
    }

    @GetMapping("/all")
    //@PreAuthorize("hasRole('Read-Roles')")
    public RoleMyResponse getRoles(@RequestParam(value="page", defaultValue = "1") int page,
                                   @RequestParam(value="limit", defaultValue = "25") int limit) {
        return roleService.getRoles(page,limit);
    }

    @DeleteMapping(path="/{roleUuid}")
    //    @PreAuthorize("hasRole('Delete-Role')")
    public ResponseEntity<?> deleteRole(@PathVariable String roleUuid) {

        return roleService.deleteRole(roleUuid);
    }

    @PutMapping("delete/privileges/{roleUuid}")
    //    @PreAuthorize("hasRole('Delete-Role')")
    public ResponseEntity<?> deleteRolePrivileges(@PathVariable String roleUuid, @Valid @RequestBody AddRolePrivilegesRequest rolePrivilegesRequest) {
        return roleService.deleteRolePrivileges(roleUuid, rolePrivilegesRequest);
    }
    @PostMapping(path = "/search")
    //    @PreAuthorize("hasRole('Read-Roles')")
    public List<RoleResponse> searchRoles(@RequestParam("search") String searchKey, @RequestParam(value="page", defaultValue = "1") int page,
                                          @RequestParam(value="limit", defaultValue = "500") int limit){
        return roleService.searchRoles(searchKey,page,limit);

    }

}
