package com.dacm.taskManager.service.implementedService;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.exception.CommonErrorResponse;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.responses.PasswordRequest;
import com.dacm.taskManager.service.interfaceService.EmailService;
import com.dacm.taskManager.service.interfaceService.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";


    @Override
    public void resetPassword(PasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        int passwordLength = 15; //

        String newPassword = generateRandomPassword(passwordLength);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println(newPassword);
        emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
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
