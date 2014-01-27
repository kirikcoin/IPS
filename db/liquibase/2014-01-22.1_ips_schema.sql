-- liquibase formatted SQL

-- changeset andy:3

ALTER TABLE users MODIFY password VARCHAR(255) NOT NULL;

ALTER TABLE question_options
DROP KEY `FK_qid_lime_answers`,
DROP FOREIGN KEY `FK_qid_lime_answers`,
ADD CONSTRAINT FK_question_options_questions
FOREIGN KEY (`qid`) REFERENCES questions (`qid`);

ALTER TABLE questions
DROP KEY `FK_sid_lime_questions`,
DROP FOREIGN KEY `FK_sid_lime_questions`,
ADD CONSTRAINT `FK_questions_surveys`
FOREIGN KEY (`sid`) REFERENCES surveys (`sid`);

ALTER TABLE surveys_text
DROP KEY `FK_sid_lime_surveys_languagesettings`,
DROP FOREIGN KEY `FK_sid_lime_surveys_languagesettings`,
ADD CONSTRAINT `FK_surveys_text_surveys`
FOREIGN KEY (`surveyls_survey_id`) REFERENCES surveys (`sid`);

ALTER TABLE surveys_users
DROP KEY `FK_sid_lime_surveys_rights`,
DROP FOREIGN KEY `FK_sid_lime_surveys_rights`,
ADD CONSTRAINT `FK_surveys_users_surveys`
FOREIGN KEY (`sid`) REFERENCES surveys (`sid`);

ALTER TABLE surveys_users
DROP KEY `FK_uid_lime_surveys_rights`,
DROP FOREIGN KEY `FK_uid_lime_surveys_rights`,
ADD CONSTRAINT `FK_surveys_users_users`
FOREIGN KEY (`uid`) REFERENCES users (`uid`);

ALTER TABLE survey_stats
DROP KEY `FK_pollId_ussdpoll_surveyStatistic`,
DROP FOREIGN KEY `FK_pollId_ussdpoll_surveyStatistic`,
ADD CONSTRAINT `FK_survey_stats_surveys`
FOREIGN KEY (`pollId`) REFERENCES surveys (`sid`);

