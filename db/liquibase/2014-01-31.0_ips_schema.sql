-- liquibase formatted SQL

-- changeset nedenzel:11

ALTER TABLE `users` ADD UNIQUE (users_name);
ALTER TABLE `users` ADD UNIQUE (email);