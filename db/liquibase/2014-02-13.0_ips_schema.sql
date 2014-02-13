-- liquibase formatted SQL

-- changeset andy:6

ALTER TABLE `respondents` ADD `answer_count` INT (11) NOT NULL DEFAULT 0;

ALTER TABLE `respondents` DROP `answered`;
ALTER TABLE `respondents` ADD `finished` BOOLEAN NOT NULL DEFAULT FALSE;