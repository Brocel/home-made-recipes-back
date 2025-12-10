-- V3__add_normalized_name_and_indexes.sql

-- 1. Add normalized name column to product table
ALTER TABLE product ADD COLUMN IF NOT EXISTS NORMALIZED_NAME VARCHAR(100);

-- 2. Create trigger function for name normalization
CREATE OR REPLACE FUNCTION trg_product_normalized_name()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.NORMALIZED_NAME := LOWER(TRIM(NEW.name));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 3. Create trigger on product table
DROP TRIGGER IF EXISTS trg_product_normalized_name_trigger ON product;

CREATE TRIGGER trg_product_normalized_name_trigger
    BEFORE INSERT OR UPDATE ON product
    FOR EACH ROW
EXECUTE FUNCTION trg_product_normalized_name();

-- 4. Backfill existing products with normalized names
UPDATE product SET NORMALIZED_NAME = LOWER(TRIM(name)) WHERE NORMALIZED_NAME IS NULL;

-- 5. Create unique index on normalized product name
CREATE UNIQUE INDEX IF NOT EXISTS idx_product_normalized_name_unique ON product(NORMALIZED_NAME);

-- 6. Create indexes for other tables/columns

-- ROLE table
CREATE UNIQUE INDEX IF NOT EXISTS idx_role_name_unique ON role(name);

-- APP_USER table
CREATE UNIQUE INDEX IF NOT EXISTS idx_app_user_username_unique ON app_user(username);
CREATE UNIQUE INDEX IF NOT EXISTS idx_app_user_email_unique ON app_user(email);

-- USER_ROLE junction table
CREATE INDEX IF NOT EXISTS idx_user_role_user_id ON user_role(user_id);
CREATE INDEX IF NOT EXISTS idx_user_role_role_id ON user_role(role_id);

-- RECIPE table
CREATE INDEX IF NOT EXISTS idx_recipe_user_id ON recipe(user_id);

-- INGREDIENT table
CREATE INDEX IF NOT EXISTS idx_ingredient_recipe_id ON ingredient(recipe_id);
CREATE INDEX IF NOT EXISTS idx_ingredient_product_id ON ingredient(product_id);

-- STEP table
CREATE INDEX IF NOT EXISTS idx_step_recipe_id ON step(recipe_id);
