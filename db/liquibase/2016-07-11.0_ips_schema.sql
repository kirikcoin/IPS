-- liquibase formatted SQL
-- changeset andy:20

ALTER TABLE `users`
  CHANGE COLUMN `can_send_invitations`
  `allow_invitations` BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE `users` ADD COLUMN `allow_profile_change` BOOLEAN NOT NULL DEFAULT TRUE
COMMENT 'UI profile change is allowed';

ALTER TABLE `users` ADD COLUMN `allow_c2s` BOOLEAN NOT NULL DEFAULT TRUE
COMMENT 'Manager is allowed to bind C2S numbers';

ALTER TABLE `users` ADD COLUMN `allow_overall_stats` BOOLEAN NOT NULL DEFAULT TRUE
COMMENT 'Manager & associated clients are allowed to view inter-survey statistics';

ALTER TABLE `users` ADD COLUMN `allow_telegram` BOOLEAN NOT NULL DEFAULT TRUE
COMMENT 'Manager & associated clients are allowed binding to Telegram channel';

ALTER TABLE `users` ADD COLUMN `allow_preview` BOOLEAN NOT NULL DEFAULT TRUE
COMMENT 'Manager & associated clients are allowed to preview surveys';

