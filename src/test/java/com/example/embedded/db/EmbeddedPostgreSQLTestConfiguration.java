package com.example.embedded.db;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
class EmbeddedPostgreSQLTestConfiguration {

    @Bean
    FooRepository jdbcFooRepository(DataSource dataSource) {
        return new JdbcFooRepository(dataSource);
    }
}
