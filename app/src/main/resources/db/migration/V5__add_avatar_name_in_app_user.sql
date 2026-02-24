-- V5__add_avatar_name_in_app_user.sql

ALTER TABLE app_user ADD COLUMN IF NOT EXISTS AVATAR_NAME VARCHAR(100);