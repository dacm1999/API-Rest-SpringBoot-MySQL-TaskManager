package com.dacm.taskManager.authentication;


import com.dacm.taskManager.user.Role;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.jwt.JwtService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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

    public AuthResponse register(RegisterRequest request) {

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

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .generatedToken(jwtService.getToken(user))
                .build();
    }
}
