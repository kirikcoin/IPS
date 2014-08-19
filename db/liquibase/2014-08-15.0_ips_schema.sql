-- liquibase formatted SQL
-- changeset andy:12

ALTER TABLE `users` ADD COLUMN `time_zone_id` VARCHAR(255) NOT NULL DEFAULT 'Europe/Moscow';
