package com.adapter.contentloaderadapter.service.impl;

import com.adapter.contentloaderadapter.service.ContentProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ContentProcessorServiceImpl implements ContentProcessorService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ContentProcessorServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<String> rosreestrUpload() {

        ObjectNode objectNode = objectMapper.createObjectNode(); // формируем json во время чтения xlsx
            try (FileInputStream fileInputStream = new FileInputStream("static/data/domClick.xlsx")){

            }catch (IOException e){

            }

        //выгрузка из xlsx конвертация в json отправка в кафку батчами



        kafkaTemplate.send("ros-events", "");
        return ResponseEntity.ok("Отправка файлов Росреестра прошла успешно");
    }

    @Override
    public ResponseEntity<String> cianUpload() {
        InputStream domClickStream = getClass().getClassLoader().getResourceAsStream("static/data/domClick.xlsx");
        kafkaTemplate.send("dom-events", "");
        return ResponseEntity.ok("Отправка файлов Циана прошла успешно");
    }

    @Override
    public ResponseEntity<String> domClickUpload() {
        InputStream cianStream = getClass().getClassLoader().getResourceAsStream("static/data/cian.xlsx");
        kafkaTemplate.send("cian-events", "");
        return ResponseEntity.ok("Отправка файлов ДомКлика прошла успешно");
    }
}
