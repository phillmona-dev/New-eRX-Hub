package com.medco.eprescription_out_of_stock.Service.User;
import com.medco.eprescription_out_of_stock.Dto.Request.User.*;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.PharmacistResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.UserMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.UserResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.shared.enums.UserStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    ResponseEntity<?> AuthenticateUser(AuthRequest authRequest) throws AccountNotFoundException;
    ResponseEntity<?> Save(SignUpRequest signUpRequest) throws BadRequestException;
    public ResponseEntity<MessageResponse> updateUser(String userUuid, SignUpRequest userRequest);
    public ResponseEntity<?> deleteUser(String userUuid);
    public UserResponse getUser(String userUuid);
//    public List<UserResponse> getUsers(int page, int limit);
    public UserMyResponse getUsers(int page, int limit);
    public List<UserResponse> getPayerUsers(int page, int limit);
    public ResponseEntity<?> uploadProfilePicture(UploadProfileRequest requestDetail)throws IOException;
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordDetail);
    public ResponseEntity<?> changePassword(ResetPasswordRequest resetPasswordDetail, String userUuid);
    public ResponseEntity<?> verifyAccount(String emailVerificationToken);
    // public ResponseEntity<?> reSendVerification(String email) throws AddressException, MessagingException, IOException;
    // public ResponseEntity<?> sendPasswordResetCode(String email) throws AddressException, MessagingException, IOException;
    public ResponseEntity<?> checkResetCode(ResetPasswordRequest resetPasswordDetail);
    public List<UserResponse> searchUsers(String searchKey, int page, int limit);

    void changeUserStatus(Long userId, UserStatus statusEnum);

    ResponseEntity<?> registerPhysician(PhysicianSignupRequest physicianSignupRequest);

    ResponseEntity<PharmacistResponse> registerPharmacist(PharmacistSignUpRequest pharmacistSignUpRequest);


    ResponseEntity<?> registerPatient(@Valid PatientRequest patientRequest);
}
