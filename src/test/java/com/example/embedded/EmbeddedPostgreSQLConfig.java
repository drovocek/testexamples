package com.example.embedded;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class EmbeddedPostgreSQLConfig {

    @Bean
    FooRepository jdbcFooRepository(DataSource dataSource) {
        return new JdbcFooRepository(dataSource);
    }
}
