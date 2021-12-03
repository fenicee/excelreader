package com.excelreader.demo.service;

import com.excelreader.demo.controller.ExcelReader;
import com.excelreader.demo.entity.AuditLog;
import com.excelreader.demo.entity.SimpleEmailEntity;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EmailQuartzJob extends QuartzJobBean {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    ExcelReader excelReader;

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //这里写业务
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mineHelper = null;
        try {
            mineHelper = new MimeMessageHelper(message, true);
            mineHelper.setFrom(from);
            mineHelper.setTo("121469787@qq.com");
            mineHelper.setSubject("本周的云桌面登入情况");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        List<AuditLog> auditLogs = null;
        try {
            auditLogs = excelReader.getData(true);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        List<AuditLog> selectedlogs = new ArrayList<>();
        //转换到7前天的节点
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, -7);
        date = c.getTime();
            for (AuditLog auditLog : auditLogs
            ) {
                if (auditLog.getOperate_time().after(date)) {
                    selectedlogs.add(auditLog);
                    //人员去重
                }
            }
            for (int i = 0; i < selectedlogs.size() - 1; i++) {
                // 从list中索引为 list.size()-1 开始往前遍历
                for (int j = selectedlogs.size() - 1; j > i; j--) {
                    // 进行比较
                    if (selectedlogs.get(j).getDescription().getName().equals(selectedlogs.get(i).getDescription().getName())) {
                        // 去重
                        selectedlogs.remove(j);
                    }
                }
            }
            // 从list中索引为0开始往后遍历
        try {
            mineHelper.setText(selectedlogs.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);

    }
}
