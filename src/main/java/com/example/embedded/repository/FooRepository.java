package com.example.embedded.repository;

import com.example.embedded.entity.FooEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FooRepository {

    boolean save(FooEntity saved);

    Optional<FooEntity> get(UUID id);

    List<FooEntity> getAll();
}
