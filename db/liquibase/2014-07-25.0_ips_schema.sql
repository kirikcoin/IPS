-- liquibase formatted SQL
-- changeset nedenzel:11

ALTER TABLE `question_options` ADD COLUMN `next_question` int(11) default null;
ALTER TABLE `question_options` ADD FOREIGN KEY next_question_id (next_question) REFERENCES questions (id);