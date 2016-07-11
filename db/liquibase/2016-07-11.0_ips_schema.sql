-- liquibase formatted SQL
-- changeset andy:20

ALTER TABLE `users` ADD COLUMN `allow_profile_change` BOOLEAN NOT NULL DEFAULT TRUE;

