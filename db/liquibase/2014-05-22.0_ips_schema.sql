-- liquibase formatted SQL
-- changeset nedenzel:6

ALTER TABLE users ADD can_send_invitations BOOLEAN  DEFAULT FALSE;

--
-- Table structure for table `deliveries`
--

CREATE TABLE `deliveries` (
  `id`               INT(11)      NOT NULL AUTO_INCREMENT,
  `survey_id`        INT(11)      NOT NULL,
  `date`             DATETIME DEFAULT NULL,
  `type`             VARCHAR(255) NOT NULL,
  `state`            VARCHAR(255) NOT NULL DEFAULT 'INACTIVE',
  `current_position` INT(11)      NOT NULL DEFAULT 0,
  `text`             TEXT DEFAULT NULL,
  `speed`            INT(3) DEFAULT '1',
  `input_file_name`  VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_deliveries_surveys` (`survey_id`),
  CONSTRAINT `FK_deliveries_surveys` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;

--
-- Table structure for table `delivery_abonents`
--

CREATE TABLE `delivery_subscribers` (
  `id`          INT(11)      NOT NULL AUTO_INCREMENT,
  `delivery_id` INT(11)      NOT NULL,
  `msisdn`      VARCHAR(255) NOT NULL,
  `status`      VARCHAR(255) NOT NULL DEFAULT 'NEW',
  `last_update` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_delivery_subscribers_deliveries` (`delivery_id`),
  CONSTRAINT `FK_delivery_subscribers_deliveries` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;
