package com.example.ShopAppEcomere.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
@Service
@AllArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    public CompletableFuture<Void> sendEmail(String subject, String recipient, String content) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setSubject(subject);
            helper.setTo(recipient);
            helper.setText(content);
            helper.setText(content, true);  // Cho phép HTML
            javaMailSender.send(message);
            return CompletableFuture.completedFuture(null);
        }catch (MessagingException ex){
            return CompletableFuture.failedFuture(ex);
        }
    }
}
