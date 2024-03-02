package com.dacm.taskManager.controller;

import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.responses.PasswordRequest;
import com.dacm.taskManager.responses.UserResponse;
import com.dacm.taskManager.service.implementedService.EmailServiceImpl;
import com.dacm.taskManager.service.implementedService.PasswordResetServiceImpl;
import com.dacm.taskManager.service.implementedService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password")
public class PasswordResetController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final EmailServiceImpl emailService;
    private final PasswordResetServiceImpl passwordResetService;

    @PostMapping("/reset")
    public ResponseEntity<UserResponse> resetPassword(@RequestBody PasswordRequest resetRequest) {

        try {
            UserResponse response = new UserResponse();
            passwordResetService.resetPassword(resetRequest);

            response.setMessage("Password reset successfully. Check your email for the new password.");

            return ResponseEntity.ok(response);
        } catch (NullPointerException e) {
            throw new CommonErrorResponse("Email not found " + resetRequest.getEmail(), e);
        }

    }

}
