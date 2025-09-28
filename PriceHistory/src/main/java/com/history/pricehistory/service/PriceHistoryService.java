package com.history.pricehistory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.history.pricehistory.entity.HistoryEstates;
import com.history.pricehistory.entity.HistoryEstates.PriceType;
import com.history.pricehistory.repository.HistoryEstatesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PriceHistoryService {
    private final HistoryEstatesRepository repository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public PriceHistoryService(HistoryEstatesRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = {"cian-events", "ros-events", "dom-events"})
    public void readPriceHistory(List<ConsumerRecord<String, String>> records) {
        HashSet<HistoryEstates> batch = new HashSet<>();

        for (ConsumerRecord<String, String> record : records) {
            String topic = record.topic();
            try {
                JsonNode node = mapper.readTree(record.value());

                String cadastr = node.get("cadastr").asText();
                String rawType = node.get("type").asText();
                HistoryEstates.Type type = HistoryEstates.Type.valueOf(rawType.toUpperCase());
                String square = node.get("square").asText();
                BigDecimal price = new BigDecimal(node.get("price").asText());
                PriceType priceType = getPriceType(topic);

                HistoryEstates estateToSave = new HistoryEstates();
                estateToSave.setCadastr(cadastr);
                estateToSave.setType(type);
                estateToSave.setSquare(square);
                estateToSave.setPrice(price);
                estateToSave.setPriceType(priceType);

                batch.add(estateToSave);
            }catch (JsonProcessingException e) {
                log.error("Ошибка парсинга json: " + e.getMessage());
            }
        }
        repository.saveAll(batch);
    }

    public PriceType getPriceType(String rawPriceType) {
        switch (rawPriceType) {
            case "cian-events" : return PriceType.CIAN;
            case "ros-events" : return PriceType.ROSREESTR;
            case "dom-events" : return PriceType.DOMCLICK;
        }
        throw new IllegalArgumentException("Неизвестный тип события");
    }

}