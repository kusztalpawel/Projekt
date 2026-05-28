-- liquibase formatted sql
-- changeset PK:007-alter_courses_table
ALTER TABLE courses ADD COLUMN level INT NOT NULL DEFAULT 0;
ALTER TABLE courses ADD COLUMN experience INT NOT NULL DEFAULT 0;

