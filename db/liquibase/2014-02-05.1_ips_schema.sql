-- liquibase formatted SQL

-- changeset andy:14

-- Needs to be removed as implicitly references FK.
ALTER TABLE question_options
DROP FOREIGN KEY `FK_question_options_questions`;

ALTER TABLE question_options DROP PRIMARY KEY;

-- Replace PK with `id` field.
ALTER TABLE question_options ADD `id` INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE question_options
ADD CONSTRAINT `FK_question_options_questions`
FOREIGN KEY (`question_id`) REFERENCES questions (`id`);

-- Replace `code` with order inside question.
ALTER TABLE question_options DROP COLUMN `code`;
ALTER TABLE question_options ADD `option_order` INT(11) NOT NULL;
