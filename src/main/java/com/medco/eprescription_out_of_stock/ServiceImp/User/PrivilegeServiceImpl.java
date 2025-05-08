package com.medco.eprescription_out_of_stock.ServiceImp.User;
import com.medco.eprescription_out_of_stock.Repository.Users.PrivilegeRepository;
import com.medco.eprescription_out_of_stock.Dto.Request.User.PrivilegeRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PrivilegeResponse;
import com.medco.eprescription_out_of_stock.Entitiy.User.Privilege;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Service.User.PrivilegeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Override
    public PrivilegeResponse createPrivilege(PrivilegeRequest privilege) throws BadRequestException {

        PrivilegeResponse newResponse = new PrivilegeResponse();

        if (privilegeRepository.existsByPrivilegeName(privilege.getPrivilegeName())) throw new BadRequestException("Role Name is already registered!");

        Privilege privilege1 = new Privilege();
        BeanUtils.copyProperties(privilege, privilege1);
        privilegeRepository.save(privilege1);

        BeanUtils.copyProperties(privilege1,newResponse);
        return newResponse;

    }

    @Override
    public ResponseEntity<MessageResponse> updatePrivilege(String privilegeUuid, PrivilegeRequest privilegeRequest) {

        Privilege privilege = privilegeRepository.findByPrivilegeUuid(privilegeUuid);
        List<Privilege> privilegeName = privilegeRepository.findAllByPrivilegeName(privilegeRequest.getPrivilegeName());
        List<Privilege> privDesc = privilegeRepository.findAllByPrivilegeDescription(privilegeRequest.getPrivilegeDescription());

        if(privilege == null)
            throw new RuntimeException("Privilege not found.");
        if (privDesc.size() >=2 || privilegeName.size() >=2)
            throw new RuntimeException("Privilege  name or description is exist. Update privilege to have unique name and description");

        privilege.setPrivilegeName(privilegeRequest.getPrivilegeName());
        privilege.setPrivilegeDescription(privilegeRequest.getPrivilegeDescription());
        privilege.setPrivilegeCategory(privilegeRequest.getPrivilegeCategory());

        privilegeRepository.save(privilege);

        return ResponseEntity.ok(new MessageResponse("Privilege Updated successfully!"));
    }

    @Override
    public ResponseEntity<?> deletePrivilege(String privilegeString) {

        Privilege privilege = privilegeRepository.findByPrivilegeUuid(privilegeString);
        if(privilege == null)
            throw new RuntimeException("Privilege not found.");
        privilegeRepository.delete(privilege);
        return ResponseEntity.ok(new MessageResponse("Privilege deleted permanently!"));
    }

    @Override
    public PrivilegeResponse getPrivilege(String privilegeUuid) {
        Privilege priv = privilegeRepository.findByPrivilegeUuid(privilegeUuid);
        if(priv == null)
            throw new RuntimeException("PrivilegeUuid not found.");
        PrivilegeResponse privResponse = new PrivilegeResponse();
        privResponse.setPrivilegeUuid(priv.getPrivilegeUuid());
        privResponse.setPrivilegeName(priv.getPrivilegeName());
        privResponse.setPrivilegeDescription(priv.getPrivilegeDescription());
        privResponse.setPrivilegeCategory(priv.getPrivilegeCategory());
        return privResponse;
    }

    @Override
    public PrivilegeMyResponse getPrivileges(int page, int limit, String searchKey) {
        if (page > 0) page = page - 1;

        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Privilege> privilegePage = privilegeRepository.findAll(pageRequest);
        long totalPages = privilegePage.getTotalPages();
        List<Privilege> privilegeList = privilegePage.getContent();

        PrivilegeMyResponse response = new PrivilegeMyResponse();
        response.setTotalPages(totalPages);
        List<PrivilegeResponse> privilegeResponseList = new ArrayList<>();
        for (Privilege priv : privilegeList) {
            PrivilegeResponse privResponse = new PrivilegeResponse();
            BeanUtils.copyProperties(priv, privResponse);
            privilegeResponseList.add(privResponse);
        }

        response.setResponse(privilegeResponseList);
        return response;
    }

    @Override
    public List<PrivilegeResponse> searchPrivileges(String searchTerm, int page, int limit) {


        if(page > 0) page = page - 1;
        Pageable pageRequest = PageRequest.of(page, limit, Sort.by("id").descending());

        Page<Privilege> PrivilegesPage = privilegeRepository.findByPrivilegeNameContainingOrPrivilegeDescriptionContainingOrPrivilegeCategoryContaining(searchTerm,searchTerm,searchTerm, pageRequest);


        long totalPages = PrivilegesPage.getTotalPages();
        List<Privilege> privilegeList = PrivilegesPage.getContent();
        List<PrivilegeResponse> privilegeResponse = new ArrayList<>();
        for (Privilege p : privilegeList) {
            PrivilegeResponse pr = new PrivilegeResponse();
            if(privilegeResponse.size() == 0)
                pr.setTotalPages(totalPages);
            BeanUtils.copyProperties(p, pr);
            privilegeResponse.add(pr);
        }

        return privilegeResponse;
    }


}
