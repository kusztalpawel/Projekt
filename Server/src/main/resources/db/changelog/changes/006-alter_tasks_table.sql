-- liquibase formatted sql
-- changeset PK:006-alter_tasks_table
ALTER TABLE tasks ADD COLUMN is_done BOOLEAN NOT NULL DEFAULT FALSE;

