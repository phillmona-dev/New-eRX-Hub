package com.medco.eprescription_out_of_stock.ServiceImp.User;
import com.medco.eprescription_out_of_stock.Dto.Request.User.*;
import com.medco.eprescription_out_of_stock.Dto.Response.User.*;
import com.medco.eprescription_out_of_stock.Entitiy.User.*;
import com.medco.eprescription_out_of_stock.Repository.Users.*;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Exception.EmailAlreadyExists;
import com.medco.eprescription_out_of_stock.Exception.InvalidPhoneException;
import com.medco.eprescription_out_of_stock.Security.Jwt.JwtUtils;
import com.medco.eprescription_out_of_stock.Security.Services.UserDetailsImpl;
import com.medco.eprescription_out_of_stock.Service.User.UserService;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PhysicianDetailRepository physicianDetailRepository;

    @Autowired
    PharmacistDetailRepository pharmacistDetailRepository;

    @Autowired
    RoleRequestedRepository roleRequestedRepository;

    @Autowired
    EvidenceDocumentRepository  evidenceDocumentRepository;

    @Autowired
    HealthCenterRepository healthCenterRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PatientDetailRepository patientDetailRepository;

    @Value("${file.dir-attachments}")
    private String uploadDirectory;



    @Override
    public ResponseEntity<?> AuthenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getUserUuid(),
                userDetails.getEmail(),
                userDetails.getRoleUuid(),
                userDetails.getTitle(),
                userDetails.getFirstName(),
                userDetails.getFatherName(),
                userDetails.getGrandFatherName(),
                userDetails.getGender(),
                userDetails.getMobilePhone(),
                userDetails.getUserStatus(),
                userDetails.getUserType(),
                roles


        ));
    }


    @Override
    public ResponseEntity<?> Save(SignUpRequest signUpRequest) throws BadRequestException {

        if (authRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExists("Email is already in use");
        }
        if (authRepository.existsByMobilePhone(signUpRequest.getMobilePhone())) {
            throw new InvalidPhoneException("Phone is already in use");
        }
        Role roleName = roleRepository.findByRoleUuid(signUpRequest.getRoleUuid());
        if (roleName == null) {
            throw new BadRequestException("No Role Name was found with the provided RoleUuid");
        }

        User user = new User();
        BeanUtils.copyProperties(signUpRequest, user);

        user.setRoleName(roleName.getRoleName());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        authRepository.save(user);

        return ResponseEntity.ok("User Registered Successfully. Verify Your Email Please");
    }


    @Override
    public ResponseEntity<MessageResponse> updateUser(String userUuid, SignUpRequest userRequest) {

        User user = authRepository.findByUserUuid(userUuid);

        if (user == null)
            throw new RuntimeException("User not found.");

        BeanUtils.copyProperties(userRequest, user);
        authRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Updated successfully!"));
    }


    @Override
    public ResponseEntity<?> deleteUser(String userUuid) {

        User user = authRepository.findByUserUuid(userUuid);
        if (user == null)
            throw new RuntimeException("User not found.");
        user.setDeleted(true);
        authRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User soft deleted successfully!"));
    }

    @Override
    public UserResponse getUser(String userUuid) {

        User user = authRepository.findByUserUuid(userUuid);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserMyResponse getUsers(int page, int limit) {
        if (page > 0) page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<User> usersPage = authRepository.findAll(pageableRequest);

        long totalPages = usersPage.getTotalPages();
        long totalElements = usersPage.getTotalElements();
        int pageNumber = usersPage.getNumber();

        List<User> userList = usersPage.getContent();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User u : userList) {
            UserResponse ur = new UserResponse();
            BeanUtils.copyProperties(u, ur);

            userResponseList.add(ur);
        }

        UserMyResponse response = new UserMyResponse();
        response.setTotalPages(totalPages);
        response.setTotalElements(totalElements);
        response.setPageNumber(pageNumber);
        response.setResponse(userResponseList);

        return response;
    }

    @Override
    public List<UserResponse> getPayerUsers(int page, int limit) {

        return null;
    }


    @Override
    public ResponseEntity<?> uploadProfilePicture(UploadProfileRequest requestDetail) throws IOException {
        String uploadDirectory = null;
        String uploadDir = uploadDirectory + "/profiles/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String returnValue = "Image not Saved";
        byte[] bytes = requestDetail.getProfilePicture().getBytes();

        String fileName = requestDetail.getProfilePicture().getOriginalFilename();
        String extention = (fileName.substring(fileName.lastIndexOf(".") + 1)).toLowerCase();
        String newFileName = requestDetail.getUserUuid() + "." + extention;
        Path path = Paths.get(uploadDir + newFileName);
        Files.write(path, bytes);

        User user = authRepository.findByUserUuid(requestDetail.getUserUuid());

        if (user == null)
            throw new RuntimeException("User not found.");

//        user.setProfilePicture(newFileName);
//        User updatesUserDetails = authRepository.save(user);
//        if (updatesUserDetails.getProfilePicture() != null) {
//            returnValue = "Profile picture Saved";
//        }

        return ResponseEntity.ok(new MessageResponse(returnValue));
    }


    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordDetail) {
        String returnValue = "Password not changed";
        User user = authRepository.findByEmailAndPasswordResetCode(resetPasswordDetail.getEmail(),resetPasswordDetail.getPasswordResetCode());
        if(user == null) 	throw new RuntimeException("Password reset code not found.");

        user.setPassword(encoder.encode(resetPasswordDetail.getNewPassword()));
        User passwordUpdated = authRepository.save(user);
        if(passwordUpdated != null) {
            returnValue = "Password changed successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public ResponseEntity<?> changePassword(ResetPasswordRequest resetPasswordDetail, String userUuid) {
        String returnValue = "Password not changed";
        User user = authRepository.findByUserUuid(userUuid);
        if(user == null) 	throw new RuntimeException("User not found.");

        user.setPassword(encoder.encode(resetPasswordDetail.getNewPassword()));
        User passworUpdated = authRepository.save(user);
        if(passworUpdated != null) {
            returnValue = "Password changed successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }

    @Override
    public ResponseEntity<?> verifyAccount(String emailVerificationToken) {
        String returnValue = "";
        User user = authRepository.findByEmailVerificationToken(emailVerificationToken);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }

        user.setUserStatus(UserStatus.ACTIVE);
        User updatedUser = authRepository.save(user);
        if (updatedUser.getUserStatus() == UserStatus.ACTIVE) {
            returnValue = "Account Verified Successfully";
        }
        return ResponseEntity.ok(new MessageResponse(returnValue));
    }


    @Override
    public ResponseEntity <?> checkResetCode(ResetPasswordRequest resetPasswordDetail) {
        User user = authRepository.findByEmailAndPasswordResetCode(resetPasswordDetail.getEmail(),resetPasswordDetail.getPasswordResetCode());
        if(user == null) throw new RuntimeException("Invalid Reset Code.");
        return ResponseEntity.ok(new MessageResponse("Reset Code is valid"));

    }

    @Override
    public List<UserResponse> searchUsers(String searchKey, int page, int limit) {

        if(page > 0) page = page - 1;
        String[] searchKeys = searchKey.split(" ");

        Pageable pageableRequest = PageRequest.of(page, limit, Sort.by("id").descending());
        int countSpaces = StringUtils.countOccurrencesOf(searchKey, " ");

        Page<User> usersPage = null;

        if(countSpaces == 0) {
            try {
                Status userStatusEnum = Status.valueOf("ACTIVE");
                String userStatusString = userStatusEnum.name();
                usersPage = authRepository.findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrMobilePhoneContainingOrEmailContaining(searchKey,searchKey,searchKey,searchKey,searchKey, pageableRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(countSpaces == 1){
            String firstName = searchKeys[0];
            String fatherName = searchKeys[1];
            usersPage = authRepository.findByFirstNameContainingOrFatherNameContainingOrGrandFatherNameContainingOrMobilePhoneContainingOrEmailContainingAndUserStatus(
                    searchKey, searchKey, searchKey, searchKey, searchKey, Status.ACTIVE, pageableRequest
            );




        }
        else if(countSpaces == 2) {
            String firstName = searchKeys[0];
            String fatherName = searchKeys[1];
            String grandFatherName = searchKeys[2];
            usersPage = authRepository.findByFirstNameContainingAndFatherNameContainingAndGrandFatherNameContaining(firstName,fatherName,grandFatherName,pageableRequest);
        }

        int totalPages = usersPage.getTotalPages();
        List<User> users = usersPage.getContent();
        List<UserResponse> userResponse = new ArrayList<>();
        for (User u : users) {
            UserResponse ur = new UserResponse();

            if(userResponse.size() == 0)
                ur.setTotalPages(totalPages);
            BeanUtils.copyProperties(u, ur);
            userResponse.add(ur);
        }

        return userResponse;
    }


    @Override
    public void changeUserStatus(Long userId, UserStatus statusEnum) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setUserStatus(statusEnum);
        authRepository.save(user);
    }

    @Override
    public ResponseEntity<?> registerPhysician(PhysicianSignupRequest physicianSignupRequest) {

        if (userRepository.existsByEmail(physicianSignupRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (userRepository.existsByMobilePhone(physicianSignupRequest.getMobilePhone())) {
            throw new RuntimeException("Error: Mobile Phone is already in use!");
        }

        User user = new User();
        user.setUserStatus(UserStatus.PENDING);
        BeanUtils.copyProperties(physicianSignupRequest, user);
        String roleName = getRoleNameByUuid(physicianSignupRequest.getRoleUuid());
        user.setRoleName(roleName);
        user.setPassword(encoder.encode(physicianSignupRequest.getPassword()));
        User savedUser = userRepository.save(user);

        System.out.println("User registration: " + savedUser);

        UserPhysicianDetail physician = new UserPhysicianDetail();

        physician.setUser(savedUser);
        physician.setGraduatedFrom(physicianSignupRequest.getGraduatedFrom());
        physician.setGraduationYear(physicianSignupRequest.getGraduationYear());
        physician.setEducationLevel(physicianSignupRequest.getEducationLevel());
        physician.setQualificationLevel(physicianSignupRequest.getQualificationLevel());
        physician.setLicenceExpirationDate(physicianSignupRequest.getLicenceExpirationDate());
        physician.setLicenceNo(physicianSignupRequest.getLicenceNo());
        physician.setInstitutionName(physicianSignupRequest.getInstitutionName());

        UserPhysicianDetail savedPhysician = physicianDetailRepository.save(physician);



        try {
            if (physicianSignupRequest.getFiles() != null) {
                for (MultipartFile file : physicianSignupRequest.getFiles()) {
                    if (!file.isEmpty()) {
                        String originalFileName = file.getOriginalFilename();
                        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
                        String newFileName = System.currentTimeMillis() + "_" + savedPhysician.getId() + "." + fileExtension;
                        String fullFilePath = uploadDirectory + File.separator + newFileName;

                        EvidenceDocument document = new EvidenceDocument();
                        document.setFileName(newFileName);
                        document.setFileSize(file.getSize());
                        document.setUser(savedUser);

                        try (FileOutputStream fos = new FileOutputStream(new File(fullFilePath))) {
                            fos.write(file.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException("Error saving file: " + e.getMessage(), e);
                        }

                        evidenceDocumentRepository.save(document);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("File processing error: " + ex.getMessage());
            throw new RuntimeException("Failed to process files for physician registration", ex);
        }



        RequestedRole requestedRole = new RequestedRole();
        requestedRole.setUser(savedUser);
        requestedRole.setRequestDate(LocalDateTime.now());
        requestedRole.setRoleUuid(physicianSignupRequest.getRoleUuid());
        roleRequestedRepository.save(requestedRole);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(savedUser, userResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body("Physician registered successfully");


    }




    @Override
    public ResponseEntity<PharmacistResponse> registerPharmacist(PharmacistSignUpRequest pharmacistSignUpRequest) {
        return null;


    }



    @Override
    public ResponseEntity<?> registerPatient(PatientRequest patientRequest) {


        if (userRepository.existsByEmail(patientRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        if (userRepository.existsByMobilePhone(patientRequest.getMobilePhone())) {
            throw new RuntimeException("Error: Mobile Phone is already in use!");
        }


        User user = new User();
        user.setUserStatus(UserStatus.PENDING);
        BeanUtils.copyProperties(patientRequest, user);
        String roleName = getRoleNameByUuid(patientRequest.getRoleUuid());
        user.setRoleName(roleName);
        user.setPassword(encoder.encode(patientRequest.getPassword()));
        User savedUser = userRepository.save(user);

        UserPatientDetail patient = new UserPatientDetail();
        patient.setUser(savedUser);
        patient.setMPN(patientRequest.getMPN());
        patient.setMRN(patientRequest.getMRN());
        UserPatientDetail savedPatient = patientDetailRepository.save(patient);


        return ResponseEntity.status(HttpStatus.CREATED).body("Patient registered successfully");



    }


    public String getRoleNameByUuid(String roleUuid) {
        Role role = roleRepository.findByRoleUuid(roleUuid);
        if (role == null){
            throw new RuntimeException("role not found with role uuid" + roleUuid);
        }
        return role.getRoleName();
    }


}




