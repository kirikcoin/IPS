-- liquibase formatted SQL

-- changeset nedenzel:1

CREATE TABLE lime_answers (
  qid    INTEGER      NOT NULL,
  code   VARCHAR(255) NOT NULL,
  answer TEXT         NOT NULL,
  PRIMARY KEY (qid, code)
);

CREATE TABLE lime_questions (
  qid   INTEGER      NOT NULL AUTO_INCREMENT,
  gid   INTEGER,
  type  CHAR         NOT NULL,
  title VARCHAR(255) NOT NULL,
  sid   INTEGER      NOT NULL,
  PRIMARY KEY (qid)
);

CREATE TABLE lime_surveys (
  sid              INTEGER  NOT NULL AUTO_INCREMENT,
  active           CHAR(1),
  expires          DATETIME NOT NULL,
  format           CHAR     NOT NULL,
  publicstatistics CHAR(1),
  startdate        DATETIME NOT NULL,
  PRIMARY KEY (sid)
);

CREATE TABLE lime_surveys_languagesettings (
  surveyls_description TEXT,
  surveyls_endtext     TEXT,
  surveyls_title       VARCHAR(200) NOT NULL,
  surveyls_welcometext TEXT,
  surveyls_survey_id   INTEGER      NOT NULL,
  PRIMARY KEY (surveyls_survey_id)
);

CREATE TABLE lime_surveys_rights (
  activate_survey      BIT,
  browse_response      BIT,
  define_questions     BIT,
  delete_survey        BIT,
  edit_survey_property BIT,
  export               BIT,
  sid                  INTEGER NOT NULL,
  uid                  INTEGER NOT NULL,
  PRIMARY KEY (sid, uid)
);

CREATE TABLE lime_users (
  uid        INTEGER      NOT NULL AUTO_INCREMENT,
  superadmin BIT          NOT NULL,
  email      VARCHAR(255) NOT NULL,
  full_name  VARCHAR(255),
  users_name VARCHAR(255) NOT NULL,
  password   BLOB         NOT NULL,
  PRIMARY KEY (uid)
);

CREATE TABLE ussdpoll_activeSurveyQuestion (
  active_qid INTEGER,
  sid        INTEGER NOT NULL,
  PRIMARY KEY (sid)
);

CREATE TABLE ussdpoll_questionStatistic (
  questionId    INTEGER NOT NULL,
  answerCount   INTEGER,
  resendToCount INTEGER,
  resentCount   INTEGER,
  sendToCount   INTEGER,
  sentCount     INTEGER,
  PRIMARY KEY (questionId)
);

CREATE TABLE ussdpoll_registeredMSISDN (
  id      INTEGER      NOT NULL AUTO_INCREMENT,
  aid     INTEGER      NOT NULL,
  answers INTEGER      NOT NULL,
  MSISDN  VARCHAR(255) NOT NULL,
  sid     INTEGER      NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE ussdpoll_surveyCondition (
  pollId             INTEGER NOT NULL,
  userId             INTEGER NOT NULL,
  selectedQuestionId INTEGER,
  PRIMARY KEY (pollId, userId)
);

CREATE TABLE ussdpoll_surveyStatistic (
  accessNumber    VARCHAR(255),
  answeredCount   INTEGER,
  campaign        VARCHAR(255),
  channel         VARCHAR(255),
  registeredCount INTEGER,
  sent            INTEGER,
  pollId          INTEGER NOT NULL,
  PRIMARY KEY (pollId)
);

ALTER TABLE ussdpoll_surveyCondition
ADD CONSTRAINT UK_pollId_ussdpoll_surveyCondition UNIQUE (pollId);

ALTER TABLE ussdpoll_surveyCondition
ADD CONSTRAINT UK_userId_ussdpoll_surveyCondition UNIQUE (userId);

ALTER TABLE ussdpoll_surveyCondition
ADD CONSTRAINT UK_selectedQuestionId_ussdpoll_surveyCondition UNIQUE (selectedQuestionId);

ALTER TABLE lime_answers
ADD INDEX FK_qid_lime_answers (qid),
ADD CONSTRAINT FK_qid_lime_answers
FOREIGN KEY (qid)
REFERENCES lime_questions (qid);

ALTER TABLE lime_questions
ADD INDEX FK_sid_lime_questions (sid),
ADD CONSTRAINT FK_sid_lime_questions
FOREIGN KEY (sid)
REFERENCES lime_surveys (sid);

ALTER TABLE lime_surveys_languagesettings
ADD INDEX FK_sid_lime_surveys_languagesettings (surveyls_survey_id),
ADD CONSTRAINT FK_sid_lime_surveys_languagesettings
FOREIGN KEY (surveyls_survey_id)
REFERENCES lime_surveys (sid);

ALTER TABLE lime_surveys_rights
ADD INDEX FK_sid_lime_surveys_rights (sid),
ADD CONSTRAINT FK_sid_lime_surveys_rights
FOREIGN KEY (sid)
REFERENCES lime_surveys (sid);

ALTER TABLE lime_surveys_rights
ADD INDEX FK_uid_lime_surveys_rights (uid),
ADD CONSTRAINT FK_uid_lime_surveys_rights
FOREIGN KEY (uid)
REFERENCES lime_users (uid);

ALTER TABLE ussdpoll_activeSurveyQuestion
ADD INDEX FK_qid_ussdpoll_activeSurveyQuestion (active_qid),
ADD CONSTRAINT FK_qid_ussdpoll_activeSurveyQuestion
FOREIGN KEY (active_qid)
REFERENCES lime_questions (qid);

ALTER TABLE ussdpoll_activeSurveyQuestion
ADD INDEX FK_sid_ussdpoll_activeSurveyQuestion (sid),
ADD CONSTRAINT FK_sid_ussdpoll_activeSurveyQuestion
FOREIGN KEY (sid)
REFERENCES lime_surveys (sid);

ALTER TABLE ussdpoll_surveyCondition
ADD INDEX FK_pollId_ussdpoll_surveyCondition (pollId),
ADD CONSTRAINT FK_pollId_ussdpoll_surveyCondition
FOREIGN KEY (pollId)
REFERENCES lime_surveys (sid);

ALTER TABLE ussdpoll_surveyCondition
ADD INDEX FK_userId_ussdpoll_surveyCondition (userId),
ADD CONSTRAINT FK_userId_ussdpoll_surveyCondition
FOREIGN KEY (userId)
REFERENCES lime_users (uid);

ALTER TABLE ussdpoll_surveyCondition
ADD INDEX FK_selectedQuestionId_ussdpoll_surveyCondition (selectedQuestionId),
ADD CONSTRAINT FK_selectedQuestionId_ussdpoll_surveyCondition
FOREIGN KEY (selectedQuestionId)
REFERENCES lime_questions (qid);

ALTER TABLE ussdpoll_surveyStatistic
ADD INDEX FK_pollId_ussdpoll_surveyStatistic (pollId),
ADD CONSTRAINT FK_pollId_ussdpoll_surveyStatistic
FOREIGN KEY (pollId)
REFERENCES lime_surveys (sid);
