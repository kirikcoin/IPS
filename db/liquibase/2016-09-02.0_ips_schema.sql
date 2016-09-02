-- liquibase formatted SQL
-- changeset andy:21

ALTER TABLE `pages`         MODIFY `title`        TEXT;
ALTER TABLE `surveys_text`  MODIFY `end_sms_text` TEXT;