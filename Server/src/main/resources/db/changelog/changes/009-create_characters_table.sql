-- liquibase formatted sql
-- changeset PK:009-create_characters_table
CREATE TABLE characters (
    id INT PRIMARY KEY,
    attack_points INT NOT NULL,
    defence_points INT NOT NULL,
    agility_points INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

