package com.example.embedded.kafka.simple;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@TestConfiguration
class EmbeddedKafkaIntegrationTestConfiguration {

    @Bean
    SimpleKafkaConsumer kafkaConsumer() {
        return new SimpleKafkaConsumer();
    }

    @Bean
    SimpleKafkaProducer kafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        return new SimpleKafkaProducer(kafkaTemplate);
    }
}
