package com.medco.eprescription_out_of_stock.Service.Institution;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.BranchRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.BranchUpdateRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchService {

    Branch createBranch(BranchRequest branch);
    Optional<Branch> getBranchById(Long branchId);
    List<Branch> getAllBranches();
    Branch updateBranch(Long branchId, BranchUpdateRequest branch);
    void deleteBranch(Long branchId);


}
