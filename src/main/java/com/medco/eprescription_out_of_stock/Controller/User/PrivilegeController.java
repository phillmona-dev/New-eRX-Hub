package com.medco.eprescription_out_of_stock.Controller.User;
import com.medco.eprescription_out_of_stock.Dto.Request.User.PrivilegeRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Service.User.PrivilegeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/e-prescription/users/privilege")
public class PrivilegeController {

    private PrivilegeService privilegeService;

    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }


    @PostMapping
    // @PreAuthorize("hasRole('Create-Privilege')")
    public PrivilegeResponse createPrivilege(@Valid @RequestBody PrivilegeRequest privilegeRequest) throws BadRequestException {
        return privilegeService.createPrivilege(privilegeRequest);

    }


    @GetMapping(path="/{privilegeUuid}")
    //@PreAuthorize("hasRole('Read-Privilege')")
    public PrivilegeResponse getRole(@PathVariable String privilegeUuid) {
        return privilegeService.getPrivilege(privilegeUuid);
    }

    @GetMapping("/list")
    //@PreAuthorize("hasRole('Read-Privileges')")
    public PrivilegeMyResponse getPrivileges(@RequestParam(name="search", required=false) String searchKey,
                                             @RequestParam(value="page", defaultValue = "1") int page,
                                             @RequestParam(value="limit", defaultValue = "500") int limit) {
        return privilegeService.getPrivileges(page, limit, searchKey);
    }

    @PutMapping(path="/{privilegeUuid}")
    //@PreAuthorize("hasRole('Update-Privilege')")
    public ResponseEntity<MessageResponse> updatePrivilege(@PathVariable String privilegeUuid, @Valid @RequestBody PrivilegeRequest privilegeRequest) {
        return privilegeService.updatePrivilege(privilegeUuid, privilegeRequest);

    }

    @DeleteMapping(path="/{privilegeUuid}")
    // @PreAuthorize("hasRole('Delete-Privilege')")
    public ResponseEntity<?> deletePrivilege(@PathVariable String privilegeUuid) {
        return privilegeService.deletePrivilege(privilegeUuid);

    }

    // Search privileges based on search term, page, and limit
    @GetMapping("/search")
    public ResponseEntity<List<PrivilegeResponse>> searchPrivileges(
            @RequestParam String searchKey,
            @RequestParam int page,
            @RequestParam int limit) {

        List<PrivilegeResponse> privileges = privilegeService.searchPrivileges(searchKey, page, limit);

        return ResponseEntity.ok(privileges);
    }
}
