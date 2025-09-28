package com.adapter.contentprocessor.service;

import com.adapter.contentprocessor.entity.Estate;
import com.adapter.contentprocessor.repository.EstateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContentProcessorService {

    private final EstateRepository estateRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ContentProcessorService(EstateRepository estateRepository) {
        this.estateRepository = estateRepository;
    }

    @KafkaListener(topics = {"cian-events", "ros-events", "dom-events"})
    public void listen(List<ConsumerRecord<String, String>> records) {
        HashSet<String> cadastrSet = new HashSet<>();

        for (ConsumerRecord<String, String> record : records) {
            try {
                JsonNode node = objectMapper.readTree(record.value());
                String cadastr = node.get("cadastr").asText();
                cadastrSet.add(cadastr);
            } catch (JsonProcessingException e) {
                log.error("Ошибка парсинга json: " + record.value());
            }
        }

        List<Estate> allByCadastrIn = estateRepository.findAllByCadastrIn(cadastrSet);
        Map<String, Estate> estateMap = allByCadastrIn.stream()
                .collect(Collectors.toMap(Estate::getCadastr, Function.identity()));

        for (ConsumerRecord<String, String> record : records) {
            String topic = record.topic();
            try {
                JsonNode node = objectMapper.readTree(record.value());

                String cadastr = node.get("cadastr").asText();
                String rawType = node.get("type").asText();
                String square = node.get("square").asText();
                String rawPrice = node.get("price").asText();

                Estate.Type type = Estate.Type.valueOf(rawType.toUpperCase());
                BigDecimal priceValue = new BigDecimal(rawPrice);

                Estate estate = estateMap.get(cadastr);
                if (estate == null) {
                    estate = new Estate();
                    estate.setCadastr(cadastr);
                    estateMap.put(cadastr, estate);
                }

                estate.setType(type);
                estate.setSquare(square);

                if (estate.getPrice() == null) {
                    estate.setPrice(new HashMap<>());
                }

                estate.getPrice().put(mapPriceKey(topic), priceValue);


            } catch (JsonProcessingException e) {
                log.error("Ошибка парсинга json: " + record.value());
            } catch (IllegalArgumentException e) {
                log.error("Неверное значение enum или числа: " + record.value());
            }
        }

        estateRepository.saveAll(estateMap.values());
        log.info("Сохранено {} записей", estateMap.size());
    }

    public String mapPriceKey(String topic) {
        return switch (topic) {
            case "ros-events" -> "Росреестр";
            case "cian-events" -> "ЦИАН";
            case "dom-events" -> "ДомКлик";
            default -> "Неизвестный источник";
        };
    }
}
