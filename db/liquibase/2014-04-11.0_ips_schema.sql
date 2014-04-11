-- liquibase formatted SQL
-- changeset andy:4

ALTER TABLE `surveys` MODIFY `owner_id` INT(11) NOT NULL;
