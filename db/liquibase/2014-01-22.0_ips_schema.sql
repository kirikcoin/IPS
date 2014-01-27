-- liquibase formatted SQL

-- changeset andy:2

RENAME TABLE lime_answers TO question_options;
RENAME TABLE lime_questions TO questions;
RENAME TABLE lime_surveys TO surveys;
RENAME TABLE lime_surveys_languagesettings TO surveys_text;
RENAME TABLE lime_surveys_rights TO surveys_users;
RENAME TABLE lime_users TO users;

RENAME TABLE ussdpoll_questionStatistic TO question_stats;
RENAME TABLE ussdpoll_registeredMSISDN TO respondents;
RENAME TABLE ussdpoll_surveyStatistic TO survey_stats;
