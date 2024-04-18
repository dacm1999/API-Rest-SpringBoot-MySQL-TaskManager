package com.dacm.taskManager.controller;

import com.dacm.taskManager.responses.RegisterRequest;
import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.request.LoginRequest;
import com.dacm.taskManager.service.implementedService.AuthServiceImpl;
import com.dacm.taskManager.exception.AuthErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.service.implementedService.EmailServiceImpl;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8080"})
public class AuthRestController {


    private final AuthServiceImpl authService;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

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

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Validations

        if(StringUtils.isEmpty(request.getUsername())){
            throw new IllegalArgumentException("Username must be mandatory");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        if(StringUtils.isEmpty(request.getEmail())){
            throw new IllegalArgumentException("Email must be mandatory");

        } else if(userRepository.existsByUsernameContainsIgnoreCase(request.getUsername())){
            throw new IllegalArgumentException("Username already registered.");
        }

        if (StringUtils.isEmpty(request.getPassword())) {
            throw new IllegalArgumentException("Password must be mandatory.");
        } else if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("The password must be more than 5 characters.");
        }

        sendRegistrationEmail(request.getEmail(), request.getUsername());
        return ResponseEntity.ok(authService.register(request));
    }


    private void sendRegistrationEmail(String email, String username) {
        String subject = "Registration Successful";
        String messageText = "Dear " + username + ",\n\n" +
                "Thank you for registering with us. Your account has been successfully created.\n\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n\n" +
                "You can now log in to your account and start using our services.\n\n" +
                "Regards,\nThe TaskSync Team";

        emailService.sendEmail(email, subject, messageText);
    }

}
