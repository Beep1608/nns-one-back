--formatted liquibase sql



-- Insertamos 10 usuarios de prueba
INSERT INTO users (username, password)
SELECT 
    'user_' || gs, 
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.zUbZb1a'
FROM generate_series(1, 10) AS gs
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'user_' || gs
);