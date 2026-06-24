-- liquibase formatted sql
-- changeset PK:020-add_last_login_to_users.sql
ALTER TABLE users ADD COLUMN last_login TIMESTAMP NOT NULL;