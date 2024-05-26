package com.dacm.taskManager.service.implementedService;

import com.dacm.taskManager.service.interfaceService.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    public final JavaMailSender sendMessage;
    private SimpleMailMessage message;

    @Override
    public void sendPasswordResetEmail(String toEmail, String newPassword) {

        message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom("@example.com");
        message.setSubject("Password reset");
        message.setText("Your new passwsford is: " + newPassword + "\n" +
                "Don't reply this message");

        sendMessage.send(message);
    }

    @Override
    public void sendEmail(String toEmail, String subject, String text) throws MessagingException {
        MimeMessage message = sendMessage.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


        helper.setTo(toEmail);
        helper.setFrom("tasksync@hotmail.com");
        helper.setText(text, false);
        helper.setSubject(subject);

        try {
            sendMessage.send(message);
        } catch (MailException e) {
            // Log the exception, handle it, or throw it
            throw new MessagingException("Failed to send email", e);
        }
    }
}
