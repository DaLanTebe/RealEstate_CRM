package com.crm.corecrm.service.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(CreateTaskJob.class)
                .withIdentity("createTaskJob").
                storeDurably().build();
    }

    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.dailyAtHourAndMinute(8, 0);

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("createTaskTrigger")
                .startNow()
//                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
