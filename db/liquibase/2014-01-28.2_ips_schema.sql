-- liquibase formatted SQL

-- changeset andy:9

ALTER TABLE `survey_stats` MODIFY COLUMN `answeredCount` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `survey_stats` MODIFY COLUMN `registeredCount` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `survey_stats` MODIFY COLUMN `sent` INTEGER NOT NULL DEFAULT 0;

ALTER TABLE `survey_stats` MODIFY COLUMN `channel` VARCHAR(255) NOT NULL DEFAULT 'CLIENT_BASE';
