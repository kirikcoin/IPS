 -- changeset andy:3

ALTER TABLE `users` ADD COLUMN `only_own_surveys` BOOLEAN DEFAULT FALSE;

ALTER TABLE `surveys` ADD COLUMN `owner_id` INT(11) NULL;
ALTER TABLE `surveys` ADD CONSTRAINT FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);