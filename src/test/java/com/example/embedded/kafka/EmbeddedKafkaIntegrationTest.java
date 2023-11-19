package com.example.embedded.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see <a href="https://www.baeldung.com/spring-boot-kafka-testing">Example</a>
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
@DirtiesContext
@Import(EmbeddedKafkaIntegrationTestConfiguration.class)
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private SimpleKafkaConsumer consumer;

    @Autowired
    private SimpleKafkaProducer producer;

    @Value("${test.topic}")
    private String topic;

    @Test
    void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
            throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        this.producer.send(this.topic, data);

        boolean messageConsumed = this.consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertThat(messageConsumed)
                .isTrue();
        assertThat(this.consumer.getPayload())
                .contains(data);
    }
}