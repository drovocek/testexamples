package com.example.embedded.repository;

import com.example.embedded.entity.FooEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcFooRepository implements FooRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFooRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean save(FooEntity saved) {
        int result = this.jdbcTemplate.update(
                "INSERT INTO FOO VALUES (?, ?, ?)",
                saved.getFooId(), saved.getFooName(), saved.getFooNumber());
        return result > 0;
    }

    @Override
    public Optional<FooEntity> get(UUID id) {
        return Optional.ofNullable(
                this.jdbcTemplate.queryForObject("SELECT * FROM FOO WHERE foo_id = ?",
                        new Object[]{id},
                        (rs, rowNum) -> new FooEntity(
                                rs.getObject("FOO_ID", UUID.class),
                                rs.getString("FOO_NAME"),
                                rs.getInt("FOO_NUMBER")
                        )));
    }

    @Override
    public List<FooEntity> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM FOO",
                (rs, rowNum) -> new FooEntity(
                        rs.getObject("FOO_ID", UUID.class),
                        rs.getString("FOO_NAME"),
                        rs.getInt("FOO_NUMBER")
                ));
    }
}