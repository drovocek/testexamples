package com.example.embedded.kafka.foo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

class FooDtoSerializerTest {

    @Test
    void serialize() throws IOException {
        try (FooDtoSerializer fooDtoSerializer = new FooDtoSerializer()) {
            FooDto foo = new FooDto(UUID.randomUUID(), "test_name", 1);
            byte[] data = fooDtoSerializer.serialize("topic_name", foo);
            ObjectMapper objectMapper = new ObjectMapper();
            FooDto actual = objectMapper.readValue(data, FooDto.class);

            Assertions.assertThat(actual)
                    .isEqualTo(foo);
        }
    }
}