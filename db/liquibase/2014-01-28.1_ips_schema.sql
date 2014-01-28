-- liquibase formatted SQL

-- changeset andy:8

ALTER TABLE `surveys` CHANGE `active` `active` BOOLEAN NOT NULL DEFAULT TRUE;

