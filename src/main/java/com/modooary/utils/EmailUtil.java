package com.modooary.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public void sendMail(
            String email, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom("noreply@modooary.com");
        message.setRecipients(Message.RecipientType.TO, email);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }
}
