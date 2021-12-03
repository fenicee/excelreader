package com.excelreader.demo.config;
import com.excelreader.demo.service.EmailQuartzJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail1(){
        return JobBuilder.newJob(EmailQuartzJob.class).storeDurably().build();
    }

    @Bean
    public Trigger onlineUserTrigger()
    {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(24*7).repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail1()).withSchedule(simpleScheduleBuilder)
                .build();
    }


}
