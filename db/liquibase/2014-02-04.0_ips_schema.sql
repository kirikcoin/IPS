-- liquibase formatted SQL

-- changeset andy:12

ALTER TABLE `questions` DROP COLUMN `type`;
ALTER TABLE `questions` DROP COLUMN `gid`;