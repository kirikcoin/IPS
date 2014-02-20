-- liquibase formatted SQL

-- changeset nedenzel:11

ALTER TABLE `survey_stats` ADD `update_status` varchar(255) NOT NULL DEFAULT 'undefined';
