package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.entity.User;
import com.dacm.taskManager.repository.UserRepository;
import com.dacm.taskManager.request.PasswordRequest;
import com.dacm.taskManager.request.PasswordResetRequest;
import com.dacm.taskManager.responses.UserResponse;
import com.dacm.taskManager.service.interfaceService.EmailService;
import com.dacm.taskManager.service.interfaceService.PasswordService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserResponse resetPassword(PasswordResetRequest request) {
        UserResponse response = new UserResponse();
        if(request.getEmail() == null || request.getEmail().isEmpty()){
            response.setMessage("Email is required");
            return response;
        } else if (userRepository.findByEmail(request.getEmail()) == null || userRepository.findByEmail(request.getEmail()).getEmail() == null || userRepository.findByEmail(request.getEmail()).getEmail().isEmpty()){
            response.setMessage("Email not found");
            return response;
        }

        User user = userRepository.findByEmail(request.getEmail());

        int passwordLength = 15; //

        String newPassword = generateRandomPassword(passwordLength);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        System.out.println(newPassword);
        emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
        response.setMessage("Password reset successfully. Check your email for the new password.");
        return response;
    }

    @Override
    public UserResponse updatePassword(String username, PasswordRequest passwordRequest, UserDetails userDetails) throws MessagingException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (passwordRequest.getNewPassword() == null || passwordRequest.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password is required");
        }

        if (!user.getUsername().equals(userDetails.getUsername()) &&
                !userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Access is denied");
        }

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        String encodedPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        String subject = "Password Updated Successfully";
        String messageText = "Dear " + username + ",\n\n" +
                "Your password has been successfully updated.\n\n" +
                "If you did not request this change, please contact support immediately.\n\n" +
                "Regards,\nThe TaskSync Team";

        emailService.sendEmail(user.getEmail(), subject, messageText);

        return UserResponse.builder()
                .message("Password updated successfully")
                .build();
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
