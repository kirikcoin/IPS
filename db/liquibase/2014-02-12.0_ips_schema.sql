-- liquibase formatted SQL

-- changeset andy:4

CREATE TABLE `survey_invitations` (
  `id`        INT(11)  NOT NULL AUTO_INCREMENT,
  `date`      DATETIME NOT NULL,
  `value`     INT(11)  NOT NULL,
  `survey_id` INT(11)  NOT NULL,

  PRIMARY KEY (`id`),

  CONSTRAINT `FK_survey_invitations_surveys`
  FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8;