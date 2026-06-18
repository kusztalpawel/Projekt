-- liquibase formatted sql
-- changeset PK:017-change_courses_name_unique
ALTER TABLE courses ADD CONSTRAINT uk_course_user_name UNIQUE (user_id, name);
