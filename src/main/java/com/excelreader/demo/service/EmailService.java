package com.excelreader.demo.service;

import com.excelreader.demo.entity.SimpleEmailEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSimpleMail(SimpleEmailEntity simpleEmailEntity);
}
