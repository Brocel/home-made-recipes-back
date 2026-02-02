-- V3__add_normalized_name_and_indexes.sql

-- 1. Add normalized name column to product table
ALTER TABLE PRODUCT ADD COLUMN IF NOT EXISTS NORMALIZED_NAME VARCHAR(100);

-- 2. Backfill existing products with normalized names
UPDATE PRODUCT SET NORMALIZED_NAME = LOWER(TRIM(name)) WHERE NORMALIZED_NAME IS NULL;

-- 3. Create unique index on normalized product name
CREATE UNIQUE INDEX IF NOT EXISTS idx_product_normalized_name_unique ON PRODUCT(NORMALIZED_NAME);

-- 4. Create indexes for other tables/columns
-- ROLE table
CREATE UNIQUE INDEX IF NOT EXISTS idx_role_name_unique ON ROLE(name);

-- APP_USER table
CREATE UNIQUE INDEX IF NOT EXISTS idx_app_user_username_unique ON APP_USER(username);
CREATE UNIQUE INDEX IF NOT EXISTS idx_app_user_email_unique ON APP_USER(email);

-- USER_ROLE junction table
CREATE INDEX IF NOT EXISTS idx_user_role_user_id ON USER_ROLE(user_id);
CREATE INDEX IF NOT EXISTS idx_user_role_role_id ON USER_ROLE(role_id);

-- RECIPE table
CREATE INDEX IF NOT EXISTS idx_recipe_user_id ON RECIPE(user_id);

-- INGREDIENT table
CREATE INDEX IF NOT EXISTS idx_ingredient_recipe_id ON INGREDIENT(recipe_id);
CREATE INDEX IF NOT EXISTS idx_ingredient_product_id ON INGREDIENT(product_id);

-- STEP table
CREATE INDEX IF NOT EXISTS idx_step_recipe_id ON STEP(recipe_id);
