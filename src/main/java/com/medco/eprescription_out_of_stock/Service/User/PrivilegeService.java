package com.medco.eprescription_out_of_stock.Service.User;

import com.medco.eprescription_out_of_stock.Dto.Request.User.PrivilegeRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PrivilegeService {
    public PrivilegeResponse createPrivilege(PrivilegeRequest privilegeRequest) throws BadRequestException;
    ResponseEntity<MessageResponse> updatePrivilege(String privilegeUuid, PrivilegeRequest privilegeRequest);
    public ResponseEntity<?> deletePrivilege(String privilegeUuid);
    public PrivilegeResponse getPrivilege(String privilegeUuid);
    PrivilegeMyResponse getPrivileges(int page, int limit, String searchKey);
    List<PrivilegeResponse> searchPrivileges(String searchKey, int page, int limit);
}
