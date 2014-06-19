-- liquibase formatted SQL
-- changeset nedenzel:8

ALTER TABLE `respondents` ADD `coupon` VARCHAR(255) DEFAULT NULL;
ALTER TABLE `respondents` ADD UNIQUE KEY `survey_coupon_key` (`survey_id`, `coupon`);

CREATE TABLE `survey_pattern` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) NOT NULL,
  `position`  int(11) DEFAULT '0',
  `length` int(11),
  `mode` varchar (255),
  `active` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_survey_pattern__survey` (`survey_id`),
  CONSTRAINT `FK_survey_pattern__survey` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;
