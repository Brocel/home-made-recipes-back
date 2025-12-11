-- V4__update_normalization_trigger.sql

-- enable unaccent extension (Postgres contrib)
CREATE EXTENSION IF NOT EXISTS unaccent;

-- Create a SQL function to normalize a string
CREATE OR REPLACE FUNCTION normalize_text(input_text text)
    RETURNS text
    LANGUAGE sql
    IMMUTABLE
AS
$$
SELECT regexp_replace(
               lower(unaccent(coalesce($1, ''))),
               '[^a-z0-9]',
               '',
               'g'
       );
$$;

-- 1) trim, 2) unaccent (remove diacritics), 3) lower, 4) remove non-alphanum, 5) remove spaces
CREATE OR REPLACE FUNCTION trg_product_normalized_name()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.NORMALIZED_NAME := normalize_text(NEW.NAME);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger
DROP TRIGGER IF EXISTS trg_product_normalized_name_trigger ON product;
CREATE TRIGGER trg_product_normalized_name_trigger
    BEFORE INSERT OR UPDATE
    ON product
    FOR EACH ROW
EXECUTE FUNCTION trg_product_normalized_name();

-- Update existing records
UPDATE product
SET NORMALIZED_NAME = normalize_text(NAME);