-- liquibase formatted SQL

-- changeset andy:6

ALTER TABLE `questions` ADD `question_order` INTEGER NOT NULL;

ALTER TABLE `question_stats` DROP `sendToCount`;
ALTER TABLE `question_stats` DROP `resendToCount`;

ALTER TABLE `respondents` CHANGE `answers` `answered` BOOLEAN NOT NULL DEFAULT FALSE;
