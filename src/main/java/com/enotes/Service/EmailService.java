package com.enotes.Service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendMail(String to,String subject,String content) throws Exception;
}
