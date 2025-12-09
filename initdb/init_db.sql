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
CREATE TABLE IF NOT EXISTS user_role
(
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES role (id)
);

-- PRODUCT table
CREATE TABLE IF NOT EXISTS product
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(50)  NOT NULL,
    name VARCHAR(100) NOT NULL
);

-- RECIPE table
CREATE TABLE IF NOT EXISTS recipe
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
CREATE TABLE IF NOT EXISTS ingredient
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quantity   DOUBLE PRECISION,
    unit       VARCHAR(50),
    recipe_id  BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_ingredient_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id),
    CONSTRAINT fk_ingredient_product FOREIGN KEY (product_id) REFERENCES product (id)
);

-- STEP table
CREATE TABLE IF NOT EXISTS step
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(2000) NOT NULL,
    step_order  INT           NOT NULL,
    recipe_id   BIGINT        NOT NULL,
    CONSTRAINT fk_step_recipe FOREIGN KEY (recipe_id) REFERENCES recipe (id)
);

-- INSERTS --
-- Roles
INSERT INTO ROLE (NAME)
VALUES ('ROLE_ADMIN')
ON CONFLICT (NAME) DO NOTHING;
INSERT INTO ROLE (NAME)
VALUES ('ROLE_USER')
ON CONFLICT (NAME) DO NOTHING;

-- Users
INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD, BIRTH_DATE, INSCRIPTION_DATE)
VALUES ('Admin', 'Admin', 'admin', 'admin.admin@admin.com', '$2a$10$PsozxvSB.1TNs9jrGCiqr.keAUw0NjwHw2YNwOlCthgvUt8.4WyQ6', '1988-02-03',
        CURRENT_DATE);

-- Assign admin role to admin user (assuming USER_ROLE junction table)
INSERT INTO user_role (user_id, role_id)
SELECT u.id,
       r.id
FROM app_user u,
     role r
WHERE u.username = 'admin'
  AND r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- Products
-- Vegetables
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('VEGETABLE', 'Carotte'),
       ('VEGETABLE', 'Tomate'),
       ('VEGETABLE', 'Courgette'),
       ('VEGETABLE', 'Brocoli'),
       ('VEGETABLE', 'Chou-fleur'),
       ('VEGETABLE', 'Épinards'),
       ('VEGETABLE', 'Poivron'),
       ('VEGETABLE', 'Haricot vert'),
       ('VEGETABLE', 'Aubergine');

-- Fruits
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('FRUIT', 'Pomme'),
       ('FRUIT', 'Banane'),
       ('FRUIT', 'Orange'),
       ('FRUIT', 'Fraise'),
       ('FRUIT', 'Poire'),
       ('FRUIT', 'Citron'),
       ('FRUIT', 'Mangue'),
       ('FRUIT', 'Raisin');

-- Meat
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('MEAT', 'Poulet'),
       ('MEAT', 'Bœuf'),
       ('MEAT', 'Porc'),
       ('MEAT', 'Agneau'),
       ('MEAT', 'Dinde');

-- Fish
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('FISH', 'Saumon'),
       ('FISH', 'Thon'),
       ('FISH', 'Cabillaud'),
       ('FISH', 'Merlu');

-- Dairy
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('DAIRY', 'Lait'),
       ('DAIRY', 'Beurre demi-sel'),
       ('DAIRY', 'Beurre'),
       ('DAIRY', 'Crème fraîche'),
       ('DAIRY', 'Yaourt nature');

-- Cheese
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('CHEESE', 'Mozzarella'),
       ('CHEESE', 'Comté'),
       ('CHEESE', 'Gruyère'),
       ('CHEESE', 'Parmesan'),
       ('CHEESE', 'Camembert');

-- Grains
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('GRAIN', 'Quinoa'),
       ('GRAIN', 'Semoule'),
       ('GRAIN', 'Boulgour'),
       ('GRAIN', 'Avoine');

-- Spices
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('SPICE', 'Sel'),
       ('SPICE', 'Poivre'),
       ('SPICE', 'Paprika'),
       ('SPICE', 'Cumin'),
       ('SPICE', 'Curcuma'),
       ('SPICE', 'Cannelle'),
       ('SPICE', 'Piment doux');

-- Condiments
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('CONDIMENT', 'Oignon'),
       ('CONDIMENT', 'Ail'),
       ('CONDIMENT', 'Échalote'),
       ('CONDIMENT', 'Ketchup'),
       ('CONDIMENT', 'Moutarde'),
       ('CONDIMENT', 'Mayonnaise'),
       ('CONDIMENT', 'Sauce soja'),
       ('CONDIMENT', 'Vinaigre balsamique'),
       ('CONDIMENT', 'Sauce tomate');

-- Sea Fruits
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('SEA_FRUIT', 'Crevette'),
       ('SEA_FRUIT', 'Moule'),
       ('SEA_FRUIT', 'Calamar'),
       ('SEA_FRUIT', 'Noix de Saint-Jacques');

-- Nut
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('NUT', 'Amande'),
       ('NUT', 'Noisette'),
       ('NUT', 'Noix'),
       ('NUT', 'Pistache'),
       ('NUT', 'Noix de cajou');

-- Herbs
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('HERBS', 'Basilic'),
       ('HERBS', 'Persil'),
       ('HERBS', 'Coriandre'),
       ('HERBS', 'Thym'),
       ('HERBS', 'Romarin'),
       ('HERBS', 'Origan');

-- Fat
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('FAT', 'Huile d''olive'),
       ('FAT', 'Huile de tournesol'),
       ('FAT', 'Beurre'),
       ('FAT', 'Margarine');

-- Sugar
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('SUGAR', 'Sucre en poudre'),
       ('SUGAR', 'Cassonade'),
       ('SUGAR', 'Miel'),
       ('SUGAR', 'Sirop d''érable');

-- Beverage
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('BEVERAGE', 'Eau'),
       ('BEVERAGE', 'Jus d''orange'),
       ('BEVERAGE', 'Jus de pomme'),
       ('BEVERAGE', 'Limonade'),
       ('BEVERAGE', 'Café'),
       ('BEVERAGE', 'Thé');

-- Alcohol
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('ALCOHOL', 'Vin blanc'),
       ('ALCOHOL', 'Vin rouge'),
       ('ALCOHOL', 'Bière'),
       ('ALCOHOL', 'Rhum'),
       ('ALCOHOL', 'Vodka'),
       ('ALCOHOL', 'Whisky');

-- Starches
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('STARCHES', 'Pâtes'),
       ('STARCHES', 'Riz'),
       ('STARCHES', 'Pommes de terre'),
       ('STARCHES', 'Lentilles'),
       ('STARCHES', 'Haricot rouge'),
       ('STARCHES', 'Haricot blanc'),
       ('STARCHES', 'Pois chiches');

-- Others
INSERT INTO PRODUCT (TYPE, NAME)
VALUES ('OTHER', 'Pain'),
       ('OTHER', 'Bouillon de légumes'),
       ('OTHER', 'Farine'),
       ('OTHER', 'Levure boulangère');