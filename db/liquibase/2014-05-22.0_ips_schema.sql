-- liquibase formatted SQL
-- changeset nedenzel:6

ALTER TABLE users ADD can_send_invitations BOOLEAN  DEFAULT FALSE;