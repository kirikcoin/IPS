-- liquibase formatted SQL
-- changeset andy:9

CREATE TABLE `access_numbers` (
  `id`     INT(11)      NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_access_numbers_number` (`number`)
) ENGINE =InnoDB AUTO_INCREMENT =244 DEFAULT CHARSET =utf8;

ALTER TABLE `survey_stats` DROP COLUMN `accessNumber`;
ALTER TABLE `survey_stats` ADD `number_id` INT(11) NULL;

ALTER TABLE `survey_stats`
ADD FOREIGN KEY `FK_survey_stats_number` (`number_id`) REFERENCES `access_numbers` (`id`);


ALTER TABLE `users` ADD COLUMN `esdp_login` VARCHAR(255) NULL;
ALTER TABLE `users` ADD COLUMN `esdp_password` VARCHAR(255) NULL;

UPDATE `users` SET `esdp_login` = 'ips' WHERE role = 'manager';
UPDATE `users` SET `esdp_password` = 'password_hash' WHERE role = 'manager';


ALTER TABLE `delivery_subscribers` ADD INDEX `state_idx` (`state`);

ALTER TABLE `users` ADD COLUMN `show_c2s` BOOLEAN DEFAULT FALSE;
ALTER TABLE `users` ADD INDEX `role_id` (`role`);