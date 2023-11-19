package com.example.embedded.kafka.foo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class FooDtoSerializer implements Serializer<FooDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, FooDto data) {
        try {
            if (data == null) {
                log.info("Null received at serializing");
                return null;
            }

            return this.objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing FooDto to byte[]");
        }
    }
}
