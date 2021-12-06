package com.excelreader.demo.config;


import com.excelreader.demo.service.EmailExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class EmailTask {

    @Autowired
    EmailExecService emailExecService;


    @Scheduled(cron = "0 0 8 ? * FRI")
    private void configureTasks() {
            emailExecService.executeEveryFRI();
    }
}
