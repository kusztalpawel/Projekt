-- liquibase formatted sql
-- changeset PK:029-add_constraint_user_powers.sql

ALTER TABLE user_powers ADD CONSTRAINT unique_user_power UNIQUE (user_id, power_id);