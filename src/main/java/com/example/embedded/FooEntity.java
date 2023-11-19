package com.example.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FooEntity {

    private UUID fooId;
    private String fooName;
    private Integer fooNumber;
}
