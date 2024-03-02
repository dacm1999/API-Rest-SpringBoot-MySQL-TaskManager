package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.responses.AuthResponse;
import com.dacm.taskManager.responses.LoginRequest;
import com.dacm.taskManager.responses.PasswordRequest;
import com.dacm.taskManager.responses.RegisterRequest;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.enums.Role;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.service.interfaceService.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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


    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        // Verificar si el token ha expirado

        return AuthResponse
                .builder()
                .generatedToken(token)
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .generatedToken(jwtService.getToken(user))
                .build();
    }


}
