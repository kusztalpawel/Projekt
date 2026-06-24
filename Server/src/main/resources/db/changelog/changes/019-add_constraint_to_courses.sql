-- liquibase formatted sql
-- changeset PK:019-add_constraint_to_courses.sql
CREATE UNIQUE INDEX uk_template_course_name ON courses(name) WHERE user_id IS NULL;