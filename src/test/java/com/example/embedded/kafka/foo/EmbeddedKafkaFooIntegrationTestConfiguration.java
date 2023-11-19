package com.example.embedded.kafka.foo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@TestConfiguration
class EmbeddedKafkaFooIntegrationTestConfiguration {

    @Bean
    FooKafkaConsumer kafkaConsumer() {
        return new FooKafkaConsumer();
    }

    @Bean
    FooKafkaProducer kafkaProducer(KafkaTemplate<String, FooDto> kafkaTemplate) {
        return new FooKafkaProducer(kafkaTemplate);
    }

//    @Bean
//    @Order(Integer.MIN_VALUE)
//    public ProducerFactory<String, FooDto> cProducerFactory(Map<String, Object> config) {
//        System.out.println("!!!!!!!");
//        return new DefaultKafkaProducerFactory<>(config,
//                new StringSerializer(), new FooDtoSerializer());
//    }
//
//    @Bean
//    @Order(Integer.MIN_VALUE)
//    public ConsumerFactory<String, FooDto> cConsumerFactory(Map<String, Object> config) {
//        System.out.println("#######");
//        return new DefaultKafkaConsumerFactory<>(config,
//                new StringDeserializer(), new FooDtoDeserializer());
//    }
}
