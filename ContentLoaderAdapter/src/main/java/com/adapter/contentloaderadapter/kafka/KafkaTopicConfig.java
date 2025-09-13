package com.adapter.contentloaderadapter.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public KafkaAdmin getAdminClient() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    @Bean
    public NewTopic CianTopic() {
        return new NewTopic("cian-events", 1, (short) 1);
    }
    @Bean
    public NewTopic RosreeestrTopic() {
        return new NewTopic("ros-events", 1, (short) 1);
    }
    @Bean
    public NewTopic DomclickTopic() {
        return new NewTopic("dom-events", 1, (short) 1);
    }
}
