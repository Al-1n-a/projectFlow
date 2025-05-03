-- USER
DROP SEQUENCE IF EXISTS user_seq;
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
                       id BIGINT NOT NULL PRIMARY KEY,
                       archive BOOLEAN NOT NULL,
                       email VARCHAR(255),
                       name VARCHAR(255),
                       password VARCHAR(255),
                       role VARCHAR(255) CHECK (role IN ('ADMIN','RWX', 'RW', 'R'))
);

-- BOARD
DROP SEQUENCE IF EXISTS board_seq;
CREATE SEQUENCE board_seq START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS boards CASCADE;
CREATE TABLE boards (
                        id BIGINT NOT NULL PRIMARY KEY,
                        created TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
                        modified TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
                        user_id BIGINT NOT NULL,
                        description VARCHAR(255),
                        title VARCHAR(255)
);

-- DIAGRAM
DROP SEQUENCE IF EXISTS diagram_seq;
CREATE SEQUENCE diagram_seq START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS diagrams CASCADE;
CREATE TABLE diagrams (
                          id BIGINT NOT NULL PRIMARY KEY,
                          diagram_type VARCHAR(31) NOT NULL,
                          title VARCHAR(255),
                          positionX INTEGER,
                          positionY INTEGER,
                          width INTEGER DEFAULT 300,
                          height INTEGER DEFAULT 400,
                          config JSONB NOT NULL DEFAULT '{}'::jsonb,
                          created_at TIMESTAMPTZ DEFAULT NOW(),
                          updated_at TIMESTAMPTZ DEFAULT NOW(),
                          board_id BIGINT NOT NULL
);

--внешние ключи
ALTER TABLE boards
    ADD CONSTRAINT fk_board_user
        FOREIGN KEY (user_id) REFERENCES users;

ALTER TABLE diagrams
    ADD CONSTRAINT fk_diagram_board
        FOREIGN KEY (board_id) REFERENCES boards;