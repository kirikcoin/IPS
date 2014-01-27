-- liquibase formatted SQL

-- changeset andy:4

ALTER TABLE users CHANGE `uid` `id` INTEGER NOT NULL AUTO_INCREMENT;

ALTER TABLE question_options CHANGE `qid` `question_id` INTEGER NOT NULL;

ALTER TABLE question_stats CHANGE `questionId` `question_id` INTEGER NOT NULL;

ALTER TABLE questions CHANGE `qid` `id` INTEGER NOT NULL AUTO_INCREMENT;
ALTER TABLE questions CHANGE `sid` `survey_id` INTEGER NOT NULL;

ALTER TABLE respondents CHANGE `sid` `survey_id` INTEGER NOT NULL;

ALTER TABLE survey_stats CHANGE `pollId` `survey_id` INTEGER NOT NULL;

ALTER TABLE surveys CHANGE `sid` `id` INTEGER NOT NULL AUTO_INCREMENT;

ALTER TABLE surveys_text CHANGE `surveyls_description` `description` TEXT;
ALTER TABLE surveys_text CHANGE `surveyls_welcometext` `welcome_text` TEXT;
ALTER TABLE surveys_text CHANGE `surveyls_endtext` `end_text` TEXT;
ALTER TABLE surveys_text CHANGE `surveyls_title` `title` VARCHAR(200);
ALTER TABLE surveys_text CHANGE `surveyls_survey_id` `survey_id` INTEGER NOT NULL;

ALTER TABLE surveys_users CHANGE `sid` `survey_id` INTEGER NOT NULL;
ALTER TABLE surveys_users CHANGE `uid` `user_id` INTEGER NOT NULL;

ALTER TABLE surveys_users DROP `activate_survey`;
ALTER TABLE surveys_users DROP `browse_response`;
ALTER TABLE surveys_users DROP `define_questions`;
ALTER TABLE surveys_users DROP `delete_survey`;
ALTER TABLE surveys_users DROP `edit_survey_property`;
ALTER TABLE surveys_users DROP `export`;

ALTER TABLE users ADD COLUMN `blocked` BOOLEAN NOT NULL DEFAULT FALSE;
