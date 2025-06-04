package com.enotes.Service.Impl;

import com.enotes.Service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderName;

    @Override
    public void sendMail(String to, String subject, String content) throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

         /*
            to -> receiver email
            subject -> like email verification or password reset/change
            from -> sender email
         */

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(senderName, "eNotes (Do not reply)");
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }
}
