-- liquibase formatted SQL
-- changeset andy:19

ALTER TABLE `survey_stats` ADD COLUMN `telegram_token` VARCHAR(1024) NULL;
ALTER TABLE `survey_stats` ADD COLUMN `telegram_username` VARCHAR(255) NULL;

ALTER TABLE `respondents` ADD COLUMN `source_type` VARCHAR(32) NULL;
