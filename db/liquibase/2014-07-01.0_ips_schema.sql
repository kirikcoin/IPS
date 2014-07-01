-- liquibase formatted SQL
-- changeset andy:10

ALTER TABLE `users` ADD COLUMN `esdp_provider` VARCHAR(255) NULL;
UPDATE `users` SET `esdp_provider` = 'ips';