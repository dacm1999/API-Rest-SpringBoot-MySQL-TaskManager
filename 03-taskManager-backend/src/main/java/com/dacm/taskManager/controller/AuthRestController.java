package com.dacm.taskManager.controller;

import com.dacm.taskManager.responses.RegisterRequest;
import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.request.LoginRequest;
import com.dacm.taskManager.service.implementedService.AuthServiceImpl;
import com.dacm.taskManager.exception.AuthErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.service.implementedService.EmailServiceImpl;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {


    private final AuthServiceImpl authService;

    @Operation(summary = "Login user")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws MessagingException {
        return ResponseEntity.ok(authService.register(request));
    }


}
