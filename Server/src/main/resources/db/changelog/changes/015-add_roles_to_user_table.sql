-- liquibase formatted sql
-- changeset PK:015-add_roles_to_user_table
ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';
