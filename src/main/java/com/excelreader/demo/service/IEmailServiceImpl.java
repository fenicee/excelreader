package com.excelreader.demo.service;

import com.excelreader.demo.entity.SimpleEmailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class IEmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;
    @Override
    public void sendSimpleMail(SimpleEmailEntity simpleEmailEntity) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(simpleEmailEntity.getSubject());
        message.setText(simpleEmailEntity.getContent());
        message.setTo(simpleEmailEntity.getTos());
        javaMailSender.send(message);
    }
}
