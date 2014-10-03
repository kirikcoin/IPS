-- liquibase formatted SQL
-- changeset andy:14

ALTER TABLE `users` ADD COLUMN `show_all_clients` BOOLEAN NOT NULL DEFAULT FALSE;