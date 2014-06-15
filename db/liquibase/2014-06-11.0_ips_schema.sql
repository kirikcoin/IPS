-- liquibase formatted SQL
-- changeset andy:7

ALTER TABLE `surveys_text` ADD `end_sms_enabled` BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE `surveys_text` ADD `end_sms_text` VARCHAR(255) NULL;
ALTER TABLE `surveys_text` ADD `end_sms_from` VARCHAR(255) NULL;

