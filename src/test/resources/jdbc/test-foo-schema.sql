CREATE TABLE foo
(
    foo_id     UUID PRIMARY KEY,
    foo_name   VARCHAR(50) UNIQUE NOT NULL,
    foo_number NUMERIC            NOT NULL
);