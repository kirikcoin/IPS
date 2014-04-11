-- liquibase formatted SQL
-- changeset andy:3

ALTER TABLE users ADD only_own_surveys BOOLEAN DEFAULT FALSE;
ALTER TABLE surveys ADD owner_id int(11) default NULL;
ALTER TABLE surveys ADD FOREIGN KEY surveys_owner_id (owner_id) REFERENCES users (id);