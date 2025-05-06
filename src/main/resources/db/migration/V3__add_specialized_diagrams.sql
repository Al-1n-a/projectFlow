-- Таблица для диаграммы Ганта
CREATE TABLE gantt_diagrams (
                               id BIGINT PRIMARY KEY,
                               project_start_date DATE,
                               project_end_date DATE,
                               FOREIGN KEY (id) REFERENCES diagrams(id)
);

-- Таблица задач Ганта
CREATE TABLE gantt_tasks (
                            id BIGSERIAL PRIMARY KEY,
                            title VARCHAR(255) NOT NULL,
                            start_date DATE NOT NULL,
                            end_date DATE NOT NULL,
                            progress INT,
                            dependencies VARCHAR(255),
                            diagram_id BIGINT NOT NULL,
                            FOREIGN KEY (diagram_id) REFERENCES gantt_diagrams(id)
);

-- Таблица для DFD
CREATE TABLE dfd_diagrams (
                             id BIGINT PRIMARY KEY,
                             level INT NOT NULL DEFAULT 0,
                             FOREIGN KEY (id) REFERENCES diagrams(id)
);

-- Таблица элементов DFD
CREATE TABLE dfd_elements (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             type VARCHAR(50) NOT NULL,
                             position TEXT,
                             properties TEXT,
                             diagram_id BIGINT NOT NULL,
                             FOREIGN KEY (diagram_id) REFERENCES dfd_diagrams(id)
);

-- Таблица для WBS
CREATE TABLE wbs_diagrams (
                             id BIGINT PRIMARY KEY,
                             project_code VARCHAR(50),
                             FOREIGN KEY (id) REFERENCES diagrams(id)
);

-- Таблица элементов WBS
CREATE TABLE wbs_items (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          responsible VARCHAR(255),
                          diagram_id BIGINT NOT NULL,
                          parent_id BIGINT,
                          FOREIGN KEY (diagram_id) REFERENCES wbs_diagrams(id),
                          FOREIGN KEY (parent_id) REFERENCES wbs_items(id)
);