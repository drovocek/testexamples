package com.example.embedded.kafka.foo;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
class FooKafkaConsumer {

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);
    @Getter
    private String key;
    @Getter
    private FooDto value;

    @KafkaListener(topics = "${test.topic}")
    public void receive(ConsumerRecord<String, FooDto> consumerRecord) {
        log.info("received payload='{}'", consumerRecord.toString());
        this.key = consumerRecord.key();
        this.value = consumerRecord.value();

        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    // other getters
}