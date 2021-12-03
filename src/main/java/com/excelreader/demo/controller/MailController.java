package com.excelreader.demo.controller;

import com.excelreader.demo.entity.SimpleEmailEntity;
import com.excelreader.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/simple")
    public void sendSimpleMail(@RequestBody SimpleEmailEntity simpleEmailEntity){
        emailService.sendSimpleMail(simpleEmailEntity);
    }
}
