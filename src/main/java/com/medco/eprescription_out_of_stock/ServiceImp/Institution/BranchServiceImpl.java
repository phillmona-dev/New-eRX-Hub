package com.medco.eprescription_out_of_stock.ServiceImp.Institution;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.BranchRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.BranchUpdateRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Branch;
import com.medco.eprescription_out_of_stock.Repository.Prescription.BranchRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.InstitutionRepository;
import com.medco.eprescription_out_of_stock.Service.Institution.BranchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    BranchRepository branchRepository;
    @Autowired
    InstitutionRepository institutionRepository;


    @Override
    public Branch createBranch(BranchRequest request) {

        Branch branch=new Branch();
        BeanUtils.copyProperties(request,branch);
        return branchRepository.save(branch);

    }

    @Override
    public Optional<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId);
    }

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Branch updateBranch(Long branchId, BranchUpdateRequest request) {
        Branch branch=new Branch();
        BeanUtils.copyProperties(request,branch);
        if (branchRepository.existsById(branchId)) {
            branch.setId(branchId);
            return branchRepository.save(branch);
        }
        throw new IllegalArgumentException("Branch with ID " + branchId + " does not exist");
    }

    @Override
    public void deleteBranch(Long branchId) {

        if (branchRepository.existsById(branchId)) {
            branchRepository.deleteById(branchId);
        } else {
            throw new IllegalArgumentException("Branch with ID " + branchId + " does not exist");
        }

    }
}
