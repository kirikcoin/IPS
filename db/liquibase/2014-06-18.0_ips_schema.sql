-- liquibase formatted SQL
-- changeset nedenzel:8

ALTER TABLE `respondents` ADD `coupon` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `respondents` ADD UNIQUE KEY `survey_coupon_key` (`survey_id`, `coupon`);

CREATE TABLE `survey_pattern` (
  `id`        INT(11)      NOT NULL AUTO_INCREMENT,
  `survey_id` INT(11)      NOT NULL,
  `position`  INT(11)      NOT NULL DEFAULT '0',
  `length`    INT(11)      NOT NULL,
  `mode`      VARCHAR(255) NOT NULL,
  `active`    TINYINT(1)   NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_survey_pattern_survey` (`survey_id`),
  CONSTRAINT `FK_survey_pattern_survey` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE =InnoDB AUTO_INCREMENT =244 DEFAULT CHARSET =utf8;
