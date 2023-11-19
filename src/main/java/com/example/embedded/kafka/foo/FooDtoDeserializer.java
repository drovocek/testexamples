package com.example.embedded.kafka.foo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class FooDtoDeserializer implements Deserializer<FooDto> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public FooDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.info("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(data, FooDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to FooDto");
        }
    }
}
