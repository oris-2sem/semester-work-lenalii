package com.example.service.impl;

import com.example.config.MailConfigProperties;
import com.example.dto.model.MailMessageModel;
import com.example.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    private final MailConfigProperties mailConfigProperties;

    @Override
    public void sendEmail(MailMessageModel mailMessage)  {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(mailConfigProperties.getUsername());
            mimeMessageHelper.setSubject(mailMessage.getSubject());

            mimeMessageHelper.setTo(mailMessage.getTo());
            mimeMessageHelper.setText(mailMessage.getMessage());
            this.mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
