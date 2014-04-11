-- liquibase formatted SQL
-- changeset nedenzel:2

ALTER TABLE `users` ADD `locale` varchar(255)  NOT NULL DEFAULT 'EN';