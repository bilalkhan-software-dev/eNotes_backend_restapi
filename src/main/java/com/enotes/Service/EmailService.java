package com.enotes.Service;


public interface EmailService {

    void sendMail(String to,String subject,String content) throws Exception;
}
