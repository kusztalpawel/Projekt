-- liquibase formatted sql
-- changeset PK:022-change_skins_column_in_users.sql
ALTER TABLE users DROP COLUMN skin;
ALTER TABLE users ADD COLUMN skin_id INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE;
