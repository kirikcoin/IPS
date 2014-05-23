-- liquibase formatted SQL
-- changeset nedenzel:7

--
-- Table structure for table `distributions`
--

CREATE TABLE `deliveries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) NOT NULL,
  `date` datetime default NULL,
  `type` varchar(255) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `text`  text DEFAULT NULL,
  `speed`  int(3) DEFAULT '1',
  `errors_count`  int(11),
  PRIMARY KEY (`id`),
  KEY `FK_deliveries_surveys` (`survey_id`),
  CONSTRAINT `FK_deliveries_surveys` FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;