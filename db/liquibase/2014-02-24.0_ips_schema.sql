-- liquibase formatted SQL

-- changeset andy:13

ALTER TABLE `respondents`
ADD CONSTRAINT `FK_respondents_surveys`
FOREIGN KEY (`survey_id`) REFERENCES `surveys` (`id`);
