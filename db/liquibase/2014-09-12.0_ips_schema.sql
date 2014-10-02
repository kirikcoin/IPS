-- liquibase formatted SQL
-- changeset nedenzel:13

CREATE TABLE `ui_profiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `icon` BLOB,
  `skin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `users` ADD COLUMN `ui_profile_id` INT(11) DEFAULT NULL;
ALTER TABLE `users` ADD FOREIGN KEY FK_users_ui_profiles (ui_profile_id) REFERENCES ui_profiles (id);

ALTER TABLE `users` ADD COLUMN `manager_id` INT(11) DEFAULT NULL;
ALTER TABLE `users` ADD FOREIGN KEY FK_user_manager (manager_id) REFERENCES users (id);
