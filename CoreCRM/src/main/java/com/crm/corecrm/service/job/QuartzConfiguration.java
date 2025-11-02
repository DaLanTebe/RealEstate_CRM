package com.crm.corecrm.service.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail createTaskJobDetail() {
        return JobBuilder.newJob(CreateTaskJob.class)
                .withIdentity("createTaskJob")
                .build();
    }

    @Bean
    public Trigger createTaskTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(createTaskJobDetail())
                .withIdentity("createTaskTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 0))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail outboxProcessorJobDetail() {
        return JobBuilder.newJob(OutBoxProcessorJob.class)
                .withIdentity("outboxProcessorJob")
                .build();
    }

    @Bean
    public Trigger outboxProcessorTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(outboxProcessorJobDetail())
                .withIdentity("outboxProcessorTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30))
                .startNow()
                .build();
    }
}
