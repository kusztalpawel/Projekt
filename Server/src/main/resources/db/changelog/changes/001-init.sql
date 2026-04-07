-- liquibase formatted sql
-- changeset PK:001-init
CREATE TABLE users (
    id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL UNIQUE,
    level INT,
    experience INT
);

CREATE TABLE tasks (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    time DATE,
    points INT,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE exams (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type DATE,
    points INT,
    grade FLOAT,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE achievements (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    points INT
);

CREATE TABLE user_achievements (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    achievement_id INT NOT NULL REFERENCES achievements(id) ON DELETE CASCADE
);

CREATE TABLE user_friends (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    friend_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, friend_id)
);
