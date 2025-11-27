package com.crm.corecrm.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QuartzConfiguration {

    @Bean
    public JobDetail createTaskJobDetail() {
        log.info("🟡 Создаем JobDetail: createTaskJob");
        return JobBuilder.newJob(CreateTaskJob.class)
                .withIdentity("createTaskJob")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger createTaskTrigger() {
        log.info("🟡 Создаем Trigger: createTaskTrigger");
        return TriggerBuilder.newTrigger()
                .forJob(createTaskJobDetail())
                .withIdentity("createTaskTrigger")
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(40))
                .startNow()
                .build();
    }

    @Bean
    public JobDetail outboxProcessorJobDetail() {
        return JobBuilder.newJob(OutBoxProcessorJob.class)
                .withIdentity("outboxProcessorJob")
                .storeDurably()
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
