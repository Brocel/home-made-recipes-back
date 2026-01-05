-- V5__make_password_nullable_and_add_display_name.sql

-- Rendre la colonne PASSWORD nullable et ajouter DISPLAY_NAME
ALTER TABLE APP_USER
    ALTER COLUMN PASSWORD DROP NOT NULL;

ALTER TABLE APP_USER
    ADD COLUMN DISPLAY_NAME VARCHAR(100);