package com.medco.eprescription_out_of_stock.Controller.User;
import com.medco.eprescription_out_of_stock.Dto.Request.User.AuthRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.ResetPasswordRequest;
import com.medco.eprescription_out_of_stock.Dto.Request.User.SignUpRequest;
import com.medco.eprescription_out_of_stock.Dto.Response.User.MessageResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.UserMyResponse;
import com.medco.eprescription_out_of_stock.Dto.Response.User.UserResponse;
import com.medco.eprescription_out_of_stock.Exception.BadRequestException;
import com.medco.eprescription_out_of_stock.Service.User.UserService;
import com.medco.eprescription_out_of_stock.shared.enums.Status;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/e-prescription/auth/users")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody AuthRequest authRequest) throws AccountNotFoundException {
        return userService.AuthenticateUser(authRequest);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> saveUser(@Valid @RequestBody SignUpRequest signUpRequest) throws BadRequestException {

        return userService.Save(signUpRequest);

    }


    @PutMapping("/{userUuid}")
    public ResponseEntity<MessageResponse> updateUser(
            @PathVariable String userUuid,
            @Valid @RequestBody SignUpRequest userRequest) {
        return userService.updateUser(userUuid, userRequest);
    }


    @DeleteMapping("/{userUuid}")
    public ResponseEntity<?> deleteUser(@PathVariable String userUuid) {
        return userService.deleteUser(userUuid);
    }


    @GetMapping("/{userUuid}")
    public UserResponse getUser(@PathVariable String userUuid) {
        return userService.getUser(userUuid);
    }


    @GetMapping("/all")

    public UserMyResponse getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return userService.getUsers(page, limit);
    }


    @PatchMapping("/{userId}/status")
    public String changeUserStatus(
            @PathVariable Long userId,
            @RequestParam String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            userService.changeUserStatus(userId, statusEnum);
            return "User status updated successfully to " + statusEnum + ".";
        } catch (IllegalArgumentException e) {
            return "Invalid status. Allowed values are 'ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING', 'DELETED', or 'BANNED'.";

        }
    }



    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordDetail) {
        return userService.resetPassword(resetPasswordDetail);
    }


    @PutMapping("/{userUuid}/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordDetail,
            @PathVariable String userUuid) {
        return userService.changePassword(resetPasswordDetail, userUuid);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String emailVerificationToken) {
        return userService.verifyAccount(emailVerificationToken);
    }


    @PostMapping("/check-reset-code")
    public ResponseEntity<?> checkResetCode(@Valid @RequestBody ResetPasswordRequest resetPasswordDetail) {
        return userService.checkResetCode(resetPasswordDetail);
    }


    @GetMapping("/search")
    public List<UserResponse> searchUsers(
            @RequestParam String searchKey,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return userService.searchUsers(searchKey, page, limit);
    }



}
