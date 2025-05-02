CREATE TABLE gantt_tasks (
                             id BIGSERIAL PRIMARY KEY,
                             diagram_id BIGINT NOT NULL REFERENCES diagrams(id) ON DELETE CASCADE,
                             title VARCHAR(255) NOT NULL,
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             progress FLOAT DEFAULT 0.0,
                             color VARCHAR(20),
                             dependencies JSONB
);

CREATE INDEX idx_gantt_tasks_diagram ON gantt_tasks(diagram_id);

CREATE TABLE dfd_elements (
                              id BIGSERIAL PRIMARY KEY,
                              diagram_id BIGINT NOT NULL REFERENCES diagrams(id) ON DELETE CASCADE,
                              type VARCHAR(20) NOT NULL CHECK (type IN ('PROCESS', 'DATA_STORE', 'EXTERNAL_ENTITY', 'DATA_FLOW')),
                              name VARCHAR(255) NOT NULL,
                              positionX INTEGER NOT NULL,
                              positionY INTEGER NOT NULL
);

CREATE TABLE dfd_connections (
                                 id BIGSERIAL PRIMARY KEY,
                                 diagram_id BIGINT NOT NULL REFERENCES diagrams(id) ON DELETE CASCADE,
                                 source_id BIGINT NOT NULL REFERENCES dfd_elements(id),
                                 target_id BIGINT NOT NULL REFERENCES dfd_elements(id),
                                 label VARCHAR(255)
);

