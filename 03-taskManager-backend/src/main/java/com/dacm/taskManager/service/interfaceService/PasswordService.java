package com.dacm.taskManager.service.interfaceService;

import com.dacm.taskManager.request.PasswordRequest;
import com.dacm.taskManager.request.PasswordResetRequest;
import com.dacm.taskManager.responses.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;

public interface PasswordService {

    UserResponse resetPassword(PasswordResetRequest request);
    UserResponse updatePassword(String username, PasswordRequest passwordRequest, UserDetails userDetails) throws MessagingException;
    String generateRandomPassword(int length);

}
