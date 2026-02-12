
-- Enable extension for gen_random_uuid (if you use gen_random_uuid())
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ROLE table
CREATE TABLE IF NOT EXISTS ROLE
(
    ID   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(50) NOT NULL UNIQUE
);

-- USER table
CREATE TABLE IF NOT EXISTS APP_USER
(
    ID               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    FIRST_NAME       VARCHAR(100) NOT NULL,
    LAST_NAME        VARCHAR(100) NOT NULL,
    USERNAME         VARCHAR(100) NOT NULL UNIQUE,
    EMAIL            VARCHAR(150) NOT NULL UNIQUE,
    PASSWORD         VARCHAR(255) NOT NULL,
    BIRTH_DATE       DATE,
    INSCRIPTION_DATE DATE
);

-- USER_ROLE junction table
CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES APP_USER (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES ROLE (id)
);

-- PRODUCT table
CREATE TABLE IF NOT EXISTS PRODUCT
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(50)  NOT NULL,
    name VARCHAR(100) NOT NULL
);

-- RECIPE table
CREATE TABLE IF NOT EXISTS RECIPE
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title            VARCHAR(255)  NOT NULL,
    description      VARCHAR(2000) NOT NULL,
    preparation_time INT,
    type             VARCHAR(50)   NOT NULL,
    publication_date DATE,
    user_id          UUID        NOT NULL,
    CONSTRAINT fk_recipe_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

-- INGREDIENT table
CREATE TABLE IF NOT EXISTS INGREDIENT
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity   DOUBLE PRECISION,
    unit       VARCHAR(50),
    recipe_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES RECIPE (id),
    CONSTRAINT fk_ingredient_product FOREIGN KEY (product_id) REFERENCES PRODUCT (id)
);

-- STEP table
CREATE TABLE IF NOT EXISTS STEP
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(2000) NOT NULL,
    step_order  INT           NOT NULL,
    recipe_id   BIGINT        NOT NULL,
    CONSTRAINT fk_step_recipe FOREIGN KEY (recipe_id) REFERENCES RECIPE (id)
);

-- INSERTS --
-- Roles
INSERT INTO ROLE (NAME)
VALUES ('ROLE_ADMIN')
ON CONFLICT (NAME) DO NOTHING;
INSERT INTO ROLE (NAME)
VALUES ('ROLE_USER')
ON CONFLICT (NAME) DO NOTHING;
