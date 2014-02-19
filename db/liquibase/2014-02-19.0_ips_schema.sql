-- liquibase formatted SQL

-- changeset nedenzel:10

ALTER TABLE `survey_stats` ADD `last_update` datetime DEFAULT NULL;
