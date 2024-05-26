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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
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
@RequestMapping("/v1/password")
public class PasswordResetController {

    private final PasswordServiceImpl passwordResetService;

    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
        return ResponseEntity.ok(passwordResetService.resetPassword(resetRequest));
    }

    @Operation(summary = "Update password")
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    @PutMapping("/newPassword/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable String username, @RequestBody PasswordRequest passwordRequest, @AuthenticationPrincipal UserDetails userDetails) throws MessagingException {
        UserResponse response = passwordResetService.updatePassword(username, passwordRequest, userDetails);
        return ResponseEntity.ok(response);
    }

}

