-- liquibase formatted SQL

-- changeset andy:7

ALTER TABLE `questions` ADD `sent_count` INT (11) NOT NULL DEFAULT 0;
ALTER TABLE `questions` ADD `active` BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE `question_options` ADD `active` BOOLEAN NOT NULL DEFAULT TRUE;