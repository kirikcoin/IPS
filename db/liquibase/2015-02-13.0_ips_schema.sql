-- liquibase formatted SQL
-- changeset nedenzel:16

ALTER TABLE `questions` ADD `default_question_id` int(11) DEFAULT NULL;

ALTER TABLE `questions`
ADD CONSTRAINT FK_questions_question
FOREIGN KEY (`default_question_id`) REFERENCES `questions` (`id`);

ALTER TABLE `questions` ADD `enabled_default_answer` BOOLEAN DEFAULT FALSE;

ALTER TABLE `answers` ADD `type` varchar(255) NOT NULL DEFAULT 'option_answer';
ALTER TABLE `answers` MODIFY column `option_id` int(11) DEFAULT NULL;
ALTER TABLE `answers` ADD column `answer_text` varchar(255) DEFAULT NULL;

