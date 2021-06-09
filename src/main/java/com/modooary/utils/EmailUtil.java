package com.modooary.utils;

import com.modooary.domain.Member;
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
    String address = "localhost:8080";

    //가입 메일 전송
    public void sendJoinMail(
            String name, String email, Long prememberId, String key) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress admin = new InternetAddress("noreply@modooary.com");
        admin.setPersonal("modooary");
        message.setFrom(admin);
        message.setRecipients(Message.RecipientType.TO, email);
        message.setSubject(name + "님의 모두어리 가입을 환영합니다");

        String content = new StringBuffer().append("<h2>모두어리</h2>")
                .append("링크를 눌러 가입을 완료하세요.<br>")
                .append("<a href='http://" + address + "/sign-up/confirm?id=")
                .append(prememberId)
                .append("&key=")
                .append(key)
                .append("' target='_blenk'>이메일 인증하기</a>")
                .toString();

        message.setText(content, "UTF-8", "html");
        javaMailSender.send(message);
    }

    //임시 비밀번호 메일 전송
    public void sendResetPasswordMail(Member member) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress admin = new InternetAddress("noreply@modooary.com");
        admin.setPersonal("modooary");
        message.setFrom(admin);
        message.setRecipients(Message.RecipientType.TO, member.getEmail());
        message.setSubject(member.getName() + "님의 임시 비밀번호입니다");

        String content = new StringBuffer().append("<h2>모두어리</h2>")
                .append(member.getName() + "님의 임시 비밀번호가 설정되었습니다<br>")
                .append(member.getPassword() + "<br>")
                .append("로그인 후 비밀번호를 변경해주세요<br>")
                .append("<a href='http://" + address)
                .append("' target='_blenk'>모두어리 바로가기</a>")
                .toString();

        message.setText(content, "UTF-8", "html");
        javaMailSender.send(message);
    }
}
