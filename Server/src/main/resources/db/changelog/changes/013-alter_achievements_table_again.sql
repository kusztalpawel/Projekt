-- liquibase formatted sql
-- changeset PK:013-alter_achievements_table_again
ALTER TABLE achievements ADD COLUMN code VARCHAR(50) NOT NULL UNIQUE;
ALTER TABLE user_achievements DROP COLUMN progress;
ALTER TABLE user_achievements ADD COLUMN unlocked_at TIMESTAMP NOT NULL;
