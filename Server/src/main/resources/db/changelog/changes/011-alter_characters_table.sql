-- liquibase formatted sql
-- changeset PK:011-alter_characters_table
ALTER TABLE characters ADD COLUMN health NUMERIC NOT NULL DEFAULT 0;