package com.example.embedded.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @see <a href="https://habr.com/ru/articles/742786/">Example 1</a>
 */
/**
 * @see <a href="https://blog.mimacom.com/embeddedkafka-kafka-auto-configure-springboottest-bootstrapserversproperty/">Example 2</a>
 */
@SpringBootTest
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
@EmbeddedKafka(
        bootstrapServersProperty = "spring.kafka.bootstrap-servers",
        topics = EmbeddedKafkaTest.TOPIC_NAME
)
@TestPropertySource(properties = "spring.kafka.consumer.auto-offset-reset = earliest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmbeddedKafkaTest {

    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {
    }

    static final String TOPIC_NAME = "topic";

    @Autowired
    private KafkaAdmin admin;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Autowired
    private ProducerFactory<String, String> producerFactory;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private BlockingQueue<ConsumerRecord<String, String>> consumptionQueue = new LinkedBlockingDeque<>();

    @KafkaListener(topics = TOPIC_NAME, groupId = "listener")
    private void listen(ConsumerRecord<String, String> consumerRecord) throws InterruptedException {
        consumptionQueue.put(consumerRecord);
    }

    @Test
    void testProducerAndConsumer() throws Exception {
        final String KEY = "key1", VALUE = "value1";
        try (
                Consumer<String, String> consumer = consumerFactory.createConsumer("consumer", null);
                Producer<String, String> producer = producerFactory.createProducer();
        ) {
            consumer.subscribe(List.of(TOPIC_NAME));

            producer.send(new ProducerRecord<>(TOPIC_NAME, KEY, VALUE), (metadata, exception) -> {
            }).get();

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3));

            assertThat(records).singleElement().satisfies(singleRecord -> {
                assertThat(singleRecord.key()).isEqualTo(KEY);
                assertThat(singleRecord.value()).isEqualTo(VALUE);
            });
            consumer.commitSync();
            consumer.unsubscribe();
        }
    }

    @Test
    void testTemplateAndListener() throws Exception {
        final String KEY = "key2", VALUE = "value2";
        consumptionQueue.clear();

        kafkaTemplate.send(TOPIC_NAME, KEY, VALUE).get();
        ConsumerRecord<String, String> consumerRecord = consumptionQueue.poll(3, TimeUnit.SECONDS);

        assertThat(consumerRecord).isNotNull();
        assertThat(consumerRecord.key()).isEqualTo(KEY);
        assertThat(consumerRecord.value()).isEqualTo(VALUE);
        assertThat(consumptionQueue).isEmpty();
    }

    @Test
    void checkBootstrapServersParameterResolutionExample(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Autowired EmbeddedKafkaBroker broker
    ) throws Exception {
        assertThat(broker.getBrokersAsString()).isEqualTo(bootstrapServers);
    }

    @Test
    void testAdmin() {
        assertThat(admin.describeTopics(TOPIC_NAME)).containsKey(TOPIC_NAME);
    }

}