package com.example.embedded.kafka.foo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
class FooDto {

    private UUID fooId;
    private String fooName;
    private Integer fooNumber;
}