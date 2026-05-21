-- liquibase formatted sql
-- changeset PK:004-create_course_table
CREATE TABLE courses (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE tasks ADD COLUMN course_id INT NOT NULL REFERENCES courses(id) ON DELETE CASCADE;
ALTER TABLE tasks DROP COLUMN user_id;