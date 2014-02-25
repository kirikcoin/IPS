-- liquibase formatted SQL

-- changeset andy:8

ALTER TABLE `survey_stats` DROP `answeredCount`;
ALTER TABLE `survey_stats` DROP `registeredCount`;
