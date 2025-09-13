package com.adapter.contentprocessor.kafka;

import org.springframework.stereotype.Service;

@Service
public class KafkaListener {

    @org.springframework.kafka.annotation.KafkaListener(topics = {"cian-events", "ros-events","dom-events"})
    public void consumer(String jsonBuilding) {

    }
}
