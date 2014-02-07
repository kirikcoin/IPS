-- liquibase formatted SQL

-- changeset andy:15

CREATE TABLE `answers` (
  `id`            INT(11) NOT NULL AUTO_INCREMENT,
  `respondent_id` INT(11) NOT NULL,
  `question_id`   INT(11) NOT NULL,
  `option_id`     INT(11) NOT NULL,
  `timestamp`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

  PRIMARY KEY (`id`),

  KEY `FK_answers_respondents` (`respondent_id`),
  CONSTRAINT `FK_answers_respondents`
  FOREIGN KEY (`respondent_id`) REFERENCES `respondents` (`id`),

  KEY `FK_answers_questions` (`question_id`),
  CONSTRAINT `FK_answers_questions`
  FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`),

  KEY `FK_answers_question_options` (`option_id`),
  CONSTRAINT `FK_answers_question_options`
  FOREIGN KEY (`option_id`) REFERENCES `question_options` (`id`)

)
  ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8