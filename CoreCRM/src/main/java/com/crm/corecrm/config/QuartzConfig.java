package com.crm.corecrm.config;

import com.crm.corecrm.service.job.CreateTaskJob;
import com.crm.corecrm.service.job.OutBoxProcessorJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;

public class QuartzConfig {

    @Bean
    public JobDetail createTaskJobDetail() {
        return JobBuilder.newJob(CreateTaskJob.class)
                .withIdentity("createTaskJob")
                .build();
    }

    @Bean
    public Trigger createTaskTrigger() {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 0))
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
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30))
                .build();
    }
}
