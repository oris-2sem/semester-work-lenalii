package com.example.service;


import com.example.dto.model.MailMessageModel;

public interface EmailSenderService {

    void sendEmail(MailMessageModel mailMessage);
}
