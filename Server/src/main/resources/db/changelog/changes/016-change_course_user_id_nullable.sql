-- liquibase formatted sql
-- changeset PK:016-change_course_user_id_nullable
ALTER TABLE courses ALTER COLUMN user_id DROP NOT NULL;
