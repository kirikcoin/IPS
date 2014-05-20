-- liquibase formatted SQL
-- changeset andy:5

ALTER TABLE `respondents` ADD INDEX `registered_idx` (`registered`);
