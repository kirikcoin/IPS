-- liquibase formatted SQL

-- changeset andy:5

ALTER TABLE `users` DROP `superadmin`;
ALTER TABLE `users` ADD COLUMN `role` VARCHAR(255) NOT NULL;