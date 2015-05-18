-- liquibase formatted SQL
-- changeset andy:18

-- Add `survey_id' to `access_numbers'.
ALTER TABLE `access_numbers` ADD COLUMN `survey_id` INT(11) DEFAULT NULL;
ALTER TABLE `access_numbers` ADD FOREIGN KEY FK_access_numbers_surveys(`survey_id`)
REFERENCES `survey_stats` (survey_id);

-- Associate access numbers with surveys using new `survey_id' column.
UPDATE access_numbers an
SET
  an.survey_id = (
    SELECT survey_id FROM survey_stats WHERE number_id = an.id
  );

-- Remove `number_id' from `survey_stats'
ALTER TABLE `survey_stats` DROP FOREIGN KEY `survey_stats_ibfk_1`;
ALTER TABLE `survey_stats` DROP `number_id`;