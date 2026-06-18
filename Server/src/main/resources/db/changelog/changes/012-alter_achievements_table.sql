-- liquibase formatted sql
-- changeset PK:012-alter_achievements_table
ALTER TABLE achievements ADD COLUMN requirement NUMERIC NOT NULL;
ALTER TABLE user_achievements ADD COLUMN progress NUMERIC NOT NULL;