package com.crm.corecrm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    KafkaTemplate<String, Object> kafkaTemplate;

    @Bean
    public DefaultErrorHandler errorHandler() {
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DefaultErrorHandler handler = new DefaultErrorHandler(
                (record, exception) -> {
                    // Отправка в DLQ (можно внедрить KafkaTemplate)
                    kafkaTemplate.send("orders-dlt", record.value());
                },
                backOff
        );

        handler.addNotRetryableExceptions(Exception.class);
        return handler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler errorHandler) { // Спринг сам подставит наш бин

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // ВОТ ТУТ ПОДКЛЮЧАЕМ!
        factory.setCommonErrorHandler(errorHandler); // setCommonErrorHandler, а не setErrorHandler (важно!)

        return factory;
    }
}
