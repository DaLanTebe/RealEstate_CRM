package com.adapter.contentloaderadapter.service.impl;

import com.adapter.contentloaderadapter.service.ContentProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContentProcessorServiceImpl implements ContentProcessorService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ContentProcessorServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<String> process() {

        //выгрузка из xlsx конвертация в json отправка в кафку батчами



        kafkaTemplate.send("cian-events", "");
        kafkaTemplate.send("ros-events", "");
        kafkaTemplate.send("dom-events", "");
        return ResponseEntity.ok("Отправка файлов прошла успешно");
    }
}
