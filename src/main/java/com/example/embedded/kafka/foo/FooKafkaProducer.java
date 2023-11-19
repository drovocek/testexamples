package com.example.embedded.kafka.foo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
class FooKafkaProducer {

    private final KafkaTemplate<String, FooDto> kafkaTemplate;

    public void send(String topic, FooDto payload) {
        log.info("sending payload='{}' to topic='{}'", payload, topic);
        this.kafkaTemplate.send(topic, payload.getFooId().toString(), payload);
    }
}