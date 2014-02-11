-- liquibase formatted SQL

-- changeset andy:2

ALTER TABLE `question_options` ADD `terminal` BOOLEAN NOT NULL DEFAULT FALSE;