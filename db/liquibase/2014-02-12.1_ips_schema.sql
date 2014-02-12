-- liquibase formatted SQL

-- changeset andy:5

ALTER TABLE `respondents` ADD `registered` TIMESTAMP NOT NULL DEFAULT current_timestamp;