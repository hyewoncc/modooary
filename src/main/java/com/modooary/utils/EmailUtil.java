package com.modooary.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public void sendMail(
            String name, String email, Long prememberId, String key) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress admin = new InternetAddress("noreply@modooary.com");
        admin.setPersonal("modooary");
        message.setFrom(admin);
        message.setRecipients(Message.RecipientType.TO, email);
        message.setSubject(name + "님의 모두어리 가입을 환영합니다");

        String content = new StringBuffer().append("<h2>모두어리</h2>")
                .append("링크를 눌러 가입을 완료하세요.<br>")
                .append("<a href='http://localhost:8080/sign-up/confirm?id=")
                .append(prememberId)
                .append("&key=")
                .append(key)
                .append("' target='_blenk'>이메일 인증하기</a>")
                .toString();

        message.setText(content, "UTF-8", "html");
        javaMailSender.send(message);
    }
}
