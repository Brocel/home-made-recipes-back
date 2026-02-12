-- Users
INSERT INTO APP_USER (FIRST_NAME, LAST_NAME, USERNAME, EMAIL, PASSWORD, BIRTH_DATE, INSCRIPTION_DATE)
VALUES ('Admin', 'Admin', 'admin', 'admin.admin@admin.com', '$2a$10$PsozxvSB.1TNs9jrGCiqr.keAUw0NjwHw2YNwOlCthgvUt8.4WyQ6', '1988-02-03',
        CURRENT_DATE);

-- Assign admin role to admin user (assuming USER_ROLE junction table)
INSERT INTO USER_ROLE (user_id, role_id)
SELECT u.id,
       r.id
FROM app_user u,
     role r
WHERE u.username = 'admin'
  AND r.name = 'ROLE_ADMIN'
    ON CONFLICT DO NOTHING;