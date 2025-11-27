package com.crm.corecrm.service.job;

import com.crm.corecrm.entities.OutBoxEvent;
import com.crm.corecrm.repository.OutBoxRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutBoxProcessorJob implements Job {

    private final OutBoxRepository outboxEventRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutBoxProcessorJob(OutBoxRepository outboxEventRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxEventRepo = outboxEventRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Starting OutBoxProcessorJob");
        List<OutBoxEvent> pendingEvents = outboxEventRepo.findByProcessedFalse();

        pendingEvents.forEach(event -> {
            try {
                kafkaTemplate.send("notifications", event.getPayload());

                event.setProcessed(true);
                outboxEventRepo.save(event);

            } catch (Exception e) {
                log.error("Ошибка при отправке события {}", event.getId(), e);
            }
        });
    }
}