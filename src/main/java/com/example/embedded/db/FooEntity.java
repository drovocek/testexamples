package com.example.embedded.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
class FooEntity {

    private UUID fooId;
    private String fooName;
    private Integer fooNumber;
}
