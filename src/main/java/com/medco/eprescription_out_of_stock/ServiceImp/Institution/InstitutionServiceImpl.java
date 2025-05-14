package com.medco.eprescription_out_of_stock.ServiceImp.Institution;

import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.BranchUpdateRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.Prescription.Institution.InstitutionUpdateRequest;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Branch;
import com.medco.eprescription_out_of_stock.Entitiy.Prescription.Institution;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Repository.Prescription.BranchRepository;
import com.medco.eprescription_out_of_stock.Repository.Prescription.InstitutionRepository;
import com.medco.eprescription_out_of_stock.Service.Institution.InstitutionService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InstitutionServiceImpl implements InstitutionService {


    @Autowired
    BranchRepository branchRepository;
    @Autowired
    InstitutionRepository institutionRepository;


    @Override
    public ResponseEntity<?> createInstitution(InstitutionRequest request) {

        Institution institution = new Institution();
        BeanUtils.copyProperties(request,institution);
        List<Branch> branches = request.getBranches().stream()
                .map(branchRequest -> {
                    Branch br = new Branch();
                    BeanUtils.copyProperties(branchRequest, br);
                    br.setInstitution(institution);
                    return br;
                })
                .collect(Collectors.toList());
        institution.setBranches(branches);


        Institution createdInstitution = institutionRepository.save(institution);

        return ResponseEntity.ok(createdInstitution);
    }

    @Override
    public List<Institution> getAllInstitutions(String searchKey, Pageable pageable) {


        return searchKey != null ? institutionWithSearch(searchKey, pageable) : institutionWithOutSearch(pageable);

    }

    private List<Institution>  institutionWithOutSearch(Pageable pageable) {

        Page<Institution> ins = institutionRepository.findAll(pageable);
        List<Institution> institutionsList = ins.getContent();

        return institutionsList;
    }

    private List<Institution>  institutionWithSearch(String searchKey, Pageable pageable) {

        Page<Institution> ins = institutionRepository.findAllByName(searchKey,pageable);
        List<Institution> institutionsList = ins.getContent();

        return institutionsList;

    }

    @Override
    public Institution getInstitutionById(Long id) {

        return institutionRepository.getReferenceById(id);
    }


    @Override
    public Institution updateInstitution(Long id, InstitutionUpdateRequest request) {


        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Institution not found with ID: " + id));

        BeanUtils.copyProperties(request, institution, "branches");

        List<Branch> currentBranches = institution.getBranches();
        Map<Long, Branch> currentBranchMap = currentBranches.stream()
                .collect(Collectors.toMap(Branch::getId, Function.identity()));

        List<BranchUpdateRequest> requestedBranches = request.getBranches();
        List<Branch> updatedBranches = new ArrayList<>();

        for (BranchUpdateRequest branchRequest : requestedBranches) {
            Branch branch;
            if (branchRequest.getId() != null && currentBranchMap.containsKey(branchRequest.getId())) {
                branch = currentBranchMap.get(branchRequest.getId());
                BeanUtils.copyProperties(branchRequest, branch);
                branch.setInstitution(institution);
                updatedBranches.add(branch);
                currentBranchMap.remove(branchRequest.getId());
            } else {
                branch = new Branch();
                BeanUtils.copyProperties(branchRequest, branch);
                branch.setInstitution(institution);
                updatedBranches.add(branch);
            }
        }

        currentBranches.removeIf(branch -> !requestedBranches.stream()
                .anyMatch(req -> req.getId() != null && req.getId().equals(branch.getId())));

        currentBranches.addAll(updatedBranches);
        institution.setBranches(currentBranches);



        return institutionRepository.save(institution);


    }


    @Override
    public void deleteInstitution(Long id) {
        institutionRepository.deleteById(id);
    }



    //    importing file
    @Override
    public ResponseEntity<?> importInstitution(MultipartFile file) throws IOException {


        List<Institution> institutions = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(convert(file));
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    validateHeaderRow(row);
                    continue;
                }

                Institution ins = mapToInstitutionEntity(row);
                institutions.add(ins);
            }


            String successMessage= saveInstitutionFromExcel(institutions);

            return ResponseEntity.ok(successMessage);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error importing Institution: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error processing request: " + e.getMessage());
        }


    }


    @Transactional
    public String saveInstitutionFromExcel(List<Institution> institutions) {
        List<Institution> batch = new ArrayList<>();
        int batchSize = 100;
//        Logger logger = LoggerFactory.getLogger(Institutionervice.class);

        try {
            for (int i = 0; i < institutions.size(); i++) {
                batch.add(institutions.get(i));
                if (batch.size() == batchSize || i == institutions.size() - 1) {
                    institutionRepository.saveAll(batch);
                    batch.clear();
                }
            }
        } catch (Exception e) {
//            logger.error("Error saving Institution: {}", e.getMessage(), e);
            return "Error occurred while importing Institution: " + e.getMessage();
        }

        return "Imported successfully";
    }



    private Institution mapToInstitutionEntity(Row row) {

        Institution institution=new Institution();
        institution.setKebelle(String.valueOf(row.getCell(5)));
        institution.setWoreda(String.valueOf(row.getCell(6)));


        return institution;


    }


    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private void validateHeaderRow(Row row) {
        String[] headers = {
                "First Name", "Last Name", "Grandfather Name", "Date of Birth", "Gender", "Id Type", "Id Number", "Phone Number", "House Number", "Insurance Name"
        };



        for (int i = 0; i < headers.length; i++) {
            System.out.println("no error ocurred");


            if (!row.getCell(i).getStringCellValue().equalsIgnoreCase(headers[i])) {
                throw new BadRequestException("The Excel sheet for uploading Insured Members should use the format given.");
            }
        }


    }


//   end of importing




}
