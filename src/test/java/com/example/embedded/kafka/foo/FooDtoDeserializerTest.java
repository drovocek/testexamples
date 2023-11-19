package com.example.embedded.kafka.foo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FooDtoDeserializerTest {

    @Test
    void deserialize() throws JsonProcessingException {
        try (FooDtoDeserializer fooDtoDeserializer = new FooDtoDeserializer()) {
            FooDto foo = new FooDto(UUID.randomUUID(), "test_name", 1);

            //byte[] data = SerializationUtils.serialize(foo);
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] data = objectMapper.writeValueAsBytes(foo);
            FooDto actual = fooDtoDeserializer.deserialize("topic_name", data);
            assertThat(actual)
                    .isEqualTo(foo);
        }
    }
}