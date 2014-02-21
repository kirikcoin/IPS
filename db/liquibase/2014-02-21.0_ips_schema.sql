-- liquibase formatted SQL

-- changeset nedenzel:12

ALTER TABLE `users` ADD `phone_number` varchar(255);
