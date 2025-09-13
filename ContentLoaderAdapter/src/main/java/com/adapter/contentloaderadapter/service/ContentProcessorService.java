package com.adapter.contentloaderadapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ContentProcessorService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ContentProcessorService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 1000000, initialDelay = 2000)
    public void process() {
        //выгрузка из xlsx конвертация в json отправка в кафку батчами



        kafkaTemplate.send("cian-events", "");
        kafkaTemplate.send("ros-events", "");
        kafkaTemplate.send("dom-events", "");
    }
}
