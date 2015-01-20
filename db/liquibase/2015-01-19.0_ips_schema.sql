-- liquibase formatted SQL
-- changeset nedenzel:15

ALTER TABLE `deliveries` ADD `retriesEnabled` BOOLEAN DEFAULT 1;
ALTER TABLE `deliveries` ADD `retriesNumber` int(11);
ALTER TABLE `deliveries` ADD `retriesInterval` int(11);