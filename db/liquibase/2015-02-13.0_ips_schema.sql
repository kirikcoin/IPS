-- liquibase formatted SQL
-- changeset nedenzel:16

ALTER TABLE `questions` ADD `default_option_id` int(11) DEFAULT NULL;

ALTER TABLE `questions`
ADD CONSTRAINT FK_questions_question_options
FOREIGN KEY (`default_option_id`) REFERENCES `question_options` (`id`);

ALTER TABLE `answers` ADD `type` varchar(255) NOT NULL;
ALTER TABLE `answers` MODIFY column `option_id` int(11) DEFAULT NULL;
ALTER TABLE `answers` ADD column `answer_text` varchar(255) DEFAULT NULL;

