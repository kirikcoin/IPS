-- liquibase formatted SQL
-- changeset andy:9

CREATE TABLE `access_numbers` (
  `id`     INT(11)      NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_access_numbers_number` (`number`)
) ENGINE =InnoDB AUTO_INCREMENT =244 DEFAULT CHARSET =utf8;

ALTER TABLE `survey_stats` DROP COLUMN `accessNumber`;
ALTER TABLE `survey_stats` ADD `number_id` INT(11) NULL;

ALTER TABLE `survey_stats`
ADD FOREIGN KEY `FK_survey_stats_number` (`number_id`) REFERENCES `access_numbers` (`id`);