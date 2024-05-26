package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.exception.AuthErrorResponse;
import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.request.LoginRequest;
import com.dacm.taskManager.responses.RegisterRequest;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.enums.Role;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.service.interfaceService.AuthService;
import com.dacm.taskManager.service.interfaceService.EmailService;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthErrorResponse("Invalid username or password", e);
        } catch (Exception e) {
            throw new AuthErrorResponse("An error occurred while processing your request", e);
        }
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        // Verificar si el token ha expirado

        return AuthResponse
                .builder()
                .generatedToken(token)
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) throws MessagingException {

        if (StringUtils.isEmpty(request.getUsername())) {
            throw new IllegalArgumentException("Username must be mandatory");
        } else if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered.");
        }

        if (StringUtils.isEmpty(request.getEmail())) {
            throw new IllegalArgumentException("Email must be mandatory");

        } else if (userRepository.existsByUsernameContainsIgnoreCase(request.getUsername())) {
            throw new IllegalArgumentException("Username already registered.");
        }

        if (StringUtils.isEmpty(request.getPassword())) {
            throw new IllegalArgumentException("Password must be mandatory.");
        } else if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("The password must be more than 5 characters.");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(user);

        sendRegistrationEmail(request.getEmail(), request.getUsername());

        return AuthResponse.builder()
                .generatedToken(jwtService.getToken(user))
                .build();
    }

    public void sendRegistrationEmail(String email, String username) throws MessagingException {
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
