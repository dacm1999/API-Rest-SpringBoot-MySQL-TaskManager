package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.request.PasswordResetRequest;
import com.dacm.taskManager.service.interfaceService.EmailService;
import com.dacm.taskManager.service.interfaceService.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";


    @Override
    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        int passwordLength = 15; //

        String newPassword = generateRandomPassword(passwordLength);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println(newPassword);
        emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        // Retrieve the user from the database based on the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Encode the new password
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Update the user's password
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            password.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }
}
