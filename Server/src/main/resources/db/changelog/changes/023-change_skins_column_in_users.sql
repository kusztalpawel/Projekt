-- liquibase formatted sql
-- changeset PK:023-change_skins_column_in_users.sql
ALTER TABLE users DROP COLUMN skin_id;
ALTER TABLE users ADD COLUMN skin_id INT NOT NULL REFERENCES courses(id);
