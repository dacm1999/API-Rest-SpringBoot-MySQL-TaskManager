package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.service.interfaceService.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    public final JavaMailSender sendMessage;
    @Override
    public void sendPasswordResetEmail(String toEmail, String newPassword) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("example@mail.com");
        message.setSubject("Password Reset");
        message.setText("Your new password is: " + newPassword + "\n" +
                "Don't reply this message");

        sendMessage.send(message);
    }
}
