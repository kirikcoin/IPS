-- liquibase formatted SQL

-- changeset andy:7

ALTER TABLE `surveys` DROP `publicstatistics`;
ALTER TABLE `surveys` DROP `format`;
