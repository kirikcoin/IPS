-- liquibase formatted SQL
-- changeset nedenzel:8

ALTER TABLE deliveries ADD input_file_name varchar(255) NOT NULL;
ALTER TABLE deliveries ADD current_position int(11) DEFAULT 0;
