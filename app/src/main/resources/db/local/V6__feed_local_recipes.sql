------------------------------------------------------------
-- LOCAL DATASET : 2 realistic recipes per RecipeType
-- This script is ONLY executed in local environment
-- Requires:
--   - APP_USER with username 'admin'
--   - PRODUCT table populated
------------------------------------------------------------

------------------------------------------------------------
-- STARTER (Entrée)
------------------------------------------------------------
INSERT
INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Salade de tomates fraîches', 'Une salade simple et rafraîchissante avec tomates, basilic et huile d''olive.',
        10, 'STARTER', CURRENT_DATE, (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Velouté de courgettes', 'Un velouté onctueux à base de courgettes et crème fraîche.', 20, 'STARTER',
        CURRENT_DATE, (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients STARTER
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(2, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de tomates fraîches'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Tomate')),
(5, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de tomates fraîches'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Basilic')),
(10, 'MILLILITER', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de tomates fraîches'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Huile d''olive')),
(2, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Velouté de courgettes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Courgette')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Velouté de courgettes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Oignon')),
(20, 'MILLILITER', (SELECT ID FROM RECIPE WHERE TITLE = 'Velouté de courgettes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Crème fraîche'));

-- Steps STARTER
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Couper les tomates en quartiers.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de tomates fraîches')),
       ('Ajouter le basilic ciselé et l''huile d''olive.', 2,
        (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de tomates fraîches')),

       ('Couper les courgettes et l''oignon.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Velouté de courgettes')),
       ('Faire revenir puis mixer avec la crème.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Velouté de courgettes'));

------------------------------------------------------------
-- MAIN_COURSE (Plat principal)
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Poulet rôti aux herbes', 'Un poulet tendre et parfumé aux herbes.', 45, 'MAIN_COURSE', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Pâtes au brocoli', 'Un plat simple et savoureux à base de pâtes et brocoli.', 25, 'MAIN_COURSE', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients MAIN_COURSE
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Poulet rôti aux herbes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Poulet')),
(5, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Poulet rôti aux herbes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Thym')),
(5, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Poulet rôti aux herbes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Romarin')),
(200, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Pâtes au brocoli'), (SELECT ID FROM PRODUCT WHERE NAME = 'Pâtes')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Pâtes au brocoli'), (SELECT ID FROM PRODUCT WHERE NAME = 'Brocoli')),
(10, 'MILLILITER', (SELECT ID FROM RECIPE WHERE TITLE = 'Pâtes au brocoli'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Huile d''olive'));

-- Steps MAIN_COURSE
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Assaisonner le poulet et enfourner.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Poulet rôti aux herbes')),
       ('Arroser régulièrement pendant la cuisson.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Poulet rôti aux herbes')),

       ('Cuire les pâtes et le brocoli.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Pâtes au brocoli')),
       ('Mélanger avec l''huile d''olive.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Pâtes au brocoli'));

------------------------------------------------------------
-- DESSERT
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Tarte aux pommes', 'Une tarte classique et savoureuse.', 40, 'DESSERT', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Salade de fruits frais', 'Un dessert léger et vitaminé.', 15, 'DESSERT', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients DESSERT
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(3, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Tarte aux pommes'), (SELECT ID FROM PRODUCT WHERE NAME = 'Pomme')),
(50, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Tarte aux pommes'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Sucre en poudre')),
(100, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Tarte aux pommes'), (SELECT ID FROM PRODUCT WHERE NAME = 'Farine')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de fruits frais'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Banane')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de fruits frais'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Orange')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de fruits frais'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Fraise'));

-- Steps DESSERT
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Préparer la pâte et disposer les pommes.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Tarte aux pommes')),
       ('Cuire au four.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Tarte aux pommes')),

       ('Couper tous les fruits.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de fruits frais')),
       ('Mélanger délicatement.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Salade de fruits frais'));

------------------------------------------------------------
-- BEVERAGE
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Smoothie banane', 'Un smoothie doux et crémeux.', 5, 'BEVERAGE', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Citronnade maison', 'Une boisson fraîche et acidulée.', 10, 'BEVERAGE', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients BEVERAGE
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Smoothie banane'), (SELECT ID FROM PRODUCT WHERE NAME = 'Banane')),
(200, 'MILLILITER', (SELECT ID FROM RECIPE WHERE TITLE = 'Smoothie banane'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Lait')),
(2, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Citronnade maison'), (SELECT ID FROM PRODUCT WHERE NAME = 'Citron')),
(20, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Citronnade maison'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Sucre en poudre'));

-- Steps BEVERAGE
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Mixer la banane avec le lait.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Smoothie banane')),

       ('Presser les citrons et mélanger avec le sucre.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Citronnade maison')),
       ('Ajouter de l''eau fraîche.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Citronnade maison'));

------------------------------------------------------------
-- SNACK
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Toast avocat', 'Un snack rapide et sain.', 10, 'SNACK', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Yaourt granola', 'Un snack sucré et croustillant.', 5, 'SNACK', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients SNACK
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Toast avocat'), (SELECT ID FROM PRODUCT WHERE NAME = 'Pain')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Toast avocat'), (SELECT ID FROM PRODUCT WHERE NAME = 'Avocat')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Yaourt granola'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Yaourt nature')),
(30, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Yaourt granola'), (SELECT ID FROM PRODUCT WHERE NAME = 'Avoine'));

-- Steps SNACK
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Écraser l''avocat et tartiner sur le pain.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Toast avocat')),

       ('Mélanger le yaourt et le granola.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Yaourt granola'));

------------------------------------------------------------
-- APPETIZER
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Bruschetta tomate basilic', 'Un apéritif italien classique.', 15, 'APPETIZER', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Mini brochettes de poulet', 'De petites brochettes savoureuses.', 20, 'APPETIZER', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients APPETIZER
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(2, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Bruschetta tomate basilic'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Tomate')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Bruschetta tomate basilic'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Pain')),
(5, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Bruschetta tomate basilic'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Basilic')),
(200, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Mini brochettes de poulet'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Poulet')),
(5, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Mini brochettes de poulet'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Paprika'));

-- Steps APPETIZER
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Préparer la garniture tomate-basilic.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Bruschetta tomate basilic')),
       ('Griller le pain et ajouter la garniture.', 2,
        (SELECT ID FROM RECIPE WHERE TITLE = 'Bruschetta tomate basilic')),

       ('Couper le poulet et l''assaisonner.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Mini brochettes de poulet')),
       ('Former les brochettes et cuire.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Mini brochettes de poulet'));

------------------------------------------------------------
-- SIDE_DISH (Accompagnement)
------------------------------------------------------------
INSERT INTO RECIPE (TITLE, DESCRIPTION, PREPARATION_TIME, TYPE, PUBLICATION_DATE, USER_ID)
VALUES ('Purée de pommes de terre', 'Un accompagnement crémeux et classique.', 30, 'SIDE_DISH', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin')),
       ('Riz pilaf', 'Un riz parfumé et légèrement doré.', 20, 'SIDE_DISH', CURRENT_DATE,
        (SELECT ID FROM APP_USER WHERE USERNAME = 'admin'));

-- Ingredients SIDE_DISH
INSERT INTO INGREDIENT (QUANTITY, UNIT, RECIPE_ID, PRODUCT_ID)
VALUES
(3, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Purée de pommes de terre'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Pommes de terre')),
(20, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Purée de pommes de terre'),
 (SELECT ID FROM PRODUCT WHERE NAME = 'Beurre')),
(200, 'GRAM', (SELECT ID FROM RECIPE WHERE TITLE = 'Riz pilaf'), (SELECT ID FROM PRODUCT WHERE NAME = 'Riz')),
(1, 'PIECE', (SELECT ID FROM RECIPE WHERE TITLE = 'Riz pilaf'), (SELECT ID FROM PRODUCT WHERE NAME = 'Oignon'));

-- Steps SIDE_DISH
INSERT INTO STEP (DESCRIPTION, STEP_ORDER, RECIPE_ID)
VALUES ('Cuire les pommes de terre et écraser avec le beurre.', 1,
        (SELECT ID FROM RECIPE WHERE TITLE = 'Purée de pommes de terre')),

       ('Faire revenir l''oignon puis ajouter le riz.', 1, (SELECT ID FROM RECIPE WHERE TITLE = 'Riz pilaf')),
       ('Cuire jusqu''à absorption complète.', 2, (SELECT ID FROM RECIPE WHERE TITLE = 'Riz pilaf'));
