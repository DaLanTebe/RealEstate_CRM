package com.history.pricehistory.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceHistoryService {

    @KafkaListener(topics = {"cian-events", "ros-events", "dom-events"})
    public void readPriceHistory(List<ConsumerRecord<String, String>> records) {
    }
}
