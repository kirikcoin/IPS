-- liquibase formatted SQL
-- changeset nedenzel:6

ALTER TABLE users ADD can_send_invitations BOOLEAN  DEFAULT FALSE;

--
-- Table structure for table `deliveries`
--

CREATE TABLE `deliveries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) NOT NULL,
  `date` datetime default NULL,
  `type` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'INACTIVE',
  `text`  text DEFAULT NULL,
  `speed`  int(3) DEFAULT '1',
  `errors_count`  int(11),
  `input_file_name` varchar(255) NOT NULL,
  `current_position` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FK_deliveries_surveys` (`survey_id`),
  CONSTRAINT `FK_deliveries_surveys` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;

--
-- Table structure for table `delivery_abonents`
--

CREATE TABLE `delivery_abonents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delivery_id` int(11) NOT NULL,
  `msisdn` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'NEW',
  PRIMARY KEY (`id`),
  KEY `FK_delivery_abonents_deliveries` (`delivery_id`),
  CONSTRAINT `FK_delivery_abonents_deliveries` FOREIGN KEY (`delivery_id`) REFERENCES `deliveries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;
