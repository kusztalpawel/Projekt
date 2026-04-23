-- liquibase formatted sql
-- changeset PK:003-alter_user_password_column
ALTER TABLE users ALTER COLUMN password TYPE VARCHAR(255);