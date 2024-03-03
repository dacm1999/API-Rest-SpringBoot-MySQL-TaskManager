package com.dacm.taskManager.service.interfaceService;

import org.springframework.stereotype.Service;

public interface EmailService {

    void sendPasswordResetEmail(String toEmail, String token);
    void sendEmail(String to, String subject, String text);
}
