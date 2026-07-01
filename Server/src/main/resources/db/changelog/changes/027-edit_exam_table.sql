-- liquibase formatted sql
-- changeset PK:027-edit_exam_table.sql

ALTER TABLE exams ADD COLUMN created_by INT NOT NULL REFERENCES users(id) ON DELETE CASCADE;