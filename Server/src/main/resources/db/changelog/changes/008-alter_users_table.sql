-- liquibase formatted sql
-- changeset PK:008-alter_users_table
ALTER TABLE users DROP COLUMN level;
ALTER TABLE users DROP COLUMN experience;
ALTER TABLE users ADD COLUMN points INT NOT NULL DEFAULT 0;

