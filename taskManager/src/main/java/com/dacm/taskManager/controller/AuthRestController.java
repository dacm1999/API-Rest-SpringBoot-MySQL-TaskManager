package com.dacm.taskManager.controller;

import com.dacm.taskManager.responses.RegisterRequest;
import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.responses.LoginRequest;
import com.dacm.taskManager.service.implementService.AuthServiceImpl;
import com.dacm.taskManager.exception.AuthErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8080"})
public class AuthRestController {

    private final AuthServiceImpl authService;
    private final UserRepository userRepository;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (BadCredentialsException e) {
            throw new AuthErrorResponse("Invalid username or password", e);
        } catch (Exception e) {
            throw new AuthErrorResponse("An error occurred while processing your request", e);
        }
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        //Validations
        if(StringUtils.isEmpty(request.getUsername())){
            throw new IllegalArgumentException("Username must be mandatory");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        if(StringUtils.isEmpty(request.getEmail())){
            throw new IllegalArgumentException("Email must be mandatory");

        }else if(userRepository.existsByUsernameContainsIgnoreCase(request.getUsername())){
            throw new IllegalArgumentException("Username already registered.");
        }

        if (StringUtils.isEmpty(request.getPassword())) {
            throw new IllegalArgumentException("Password must be mandatory.");
        } else if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("The password must be more than 5 characters.");
        }

        return ResponseEntity.ok(authService.register(request));
    }

}
