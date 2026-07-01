-- liquibase formatted sql
-- changeset PK:030-add_last_fight_column_to_user_friends.sql

ALTER TABLE user_friends ADD COLUMN last_fight DATE;