CREATE TABLE Todo (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    done BOOLEAN NOT NULL DEFAULT (FALSE)
);
