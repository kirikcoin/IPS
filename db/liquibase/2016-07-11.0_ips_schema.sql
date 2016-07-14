-- liquibase formatted SQL
-- changeset andy:20

ALTER TABLE `users` ADD COLUMN `lk_account` BOOLEAN NOT NULL DEFAULT FALSE;

