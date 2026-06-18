-- liquibase formatted sql
-- changeset PK:018-change_achievements_table
ALTER TABLE achievements ADD COLUMN metric VARCHAR(50) NOT NULL;
ALTER TABLE achievements ADD COLUMN description VARCHAR(250);
ALTER TABLE achievements DROP COLUMN points;

ALTER TABLE users ADD COLUMN friends_count INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN tasks_completed INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN login_streak INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN levels_completed INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN skillpoints_used INT NOT NULL DEFAULT 0;

ALTER TABLE user_achievements ADD CONSTRAINT uk_user_achievement UNIQUE (user_id, achievement_id);