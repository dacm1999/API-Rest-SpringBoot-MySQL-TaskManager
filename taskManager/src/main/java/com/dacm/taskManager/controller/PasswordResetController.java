package com.dacm.taskManager.controller;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.request.PasswordRequest;
import com.dacm.taskManager.request.PasswordResetRequest;
import com.dacm.taskManager.responses.UserResponse;
import com.dacm.taskManager.service.implementedService.EmailServiceImpl;
import com.dacm.taskManager.service.implementedService.PasswordServiceImpl;
import com.dacm.taskManager.service.implementedService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordServiceImpl passwordResetService;

    @PostMapping("/reset")
    public ResponseEntity<UserResponse> resetPassword(@RequestBody PasswordResetRequest resetRequest) {

        try {
            UserResponse response = new UserResponse();
            passwordResetService.resetPassword(resetRequest);

            response.setMessage("Password reset successfully. Check your email for the new password.");

            return ResponseEntity.ok(response);
        } catch (NullPointerException e) {
            throw new CommonErrorResponse("Email not found " + resetRequest.getEmail(), e);
        }

    }

    @PutMapping("/newPassword/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable String username, @RequestBody PasswordRequest passwordRequest, @AuthenticationPrincipal UserDetails userDetails) {
        // Authenticate the user
        User user = userRepository.findFirstByUsername(username);

        // Check if the authenticated user is authorized to update the password
        if (!user.getUsername().equals(userDetails.getUsername()) && !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Access is denied");
        }

        // Validate the old password
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // Update the password
        passwordResetService.updatePassword(username, passwordRequest.getNewPassword());

        // Send email notification
        sendPasswordUpdatedEmail(user.getEmail(), username);

        // Return the updated user response
        UserResponse userResponse = UserResponse.builder()
                .message("Password updated successfully")
                .build();
        return ResponseEntity.ok(userResponse);
    }

    private void sendPasswordUpdatedEmail(String email, String username) {
        String subject = "Password Updated Successfully";
        String messageText = "Dear " + username + ",\n\n" +
                "Your password has been successfully updated.\n\n" +
                "If you did not request this change, please contact support immediately.\n\n" +
                "Regards,\nThe TaskSync Team";

        emailService.sendEmail(email, subject, messageText);
    }

}
