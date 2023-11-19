package com.example.embedded;

import com.example.embedded.entity.FooEntity;
import com.example.embedded.repository.FooRepository;
import com.example.embedded.repository.JdbcFooRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@JdbcTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@Sql({"/jdbc/test-foo-schema.sql", "/jdbc/test-foo-data.sql"})
@AutoConfigureEmbeddedDatabase(
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_EACH_TEST_METHOD,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class EmbeddedPostgresqlTest {

    @Autowired
    private DataSource embeddedDataSource;

    private FooRepository repository;

    @PostConstruct
    void config() {
        this.repository = new JdbcFooRepository(this.embeddedDataSource);
    }

    @Test
    void save() {
        FooEntity saved = new FooEntity(UUID.randomUUID(), "test_name", 1);
        boolean success = this.repository.save(saved);

        assertThat(success)
                .isTrue();
    }

    @Test
    void get() {
        UUID fooId = UUID.fromString("40e6215d-b5c6-4896-987c-f30f3678f608");
        Optional<FooEntity> entityOpt = this.repository.get(fooId);

        assertThat(entityOpt.isPresent())
                .isTrue();
        FooEntity entity = entityOpt.get();

        assertThat(entity.getFooName())
                .isEqualTo("test-name-1");
        assertThat(entity.getFooNumber())
                .isEqualTo(1);
    }

    @Test
    void getAll() {
        List<FooEntity> all = this.repository.getAll();

        assertThat(all.size())
                .isEqualTo(3);
    }
}