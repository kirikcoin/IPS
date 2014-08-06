-- liquibase formatted SQL
-- changeset nedenzel:11

ALTER TABLE `question_options` ADD COLUMN `next_question_id` INT(11) DEFAULT NULL;
ALTER TABLE `question_options` ADD FOREIGN KEY
    FK_next_question_id (`next_question`) REFERENCES `questions` (id);
ALTER TABLE `question_options` DROP COLUMN `terminal`;