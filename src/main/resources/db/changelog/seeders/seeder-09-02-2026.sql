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

--Insertamos 10 productos de prueba 
INSERT INTO products (name, description, price, stock, user_id)
SELECT 
    'Producto ' || gs, 
    'Esta es la descripción automática para el producto número ' || gs,
    (random() * (500 - 10) + 10)::decimal(12, 2), -- Precio aleatorio entre 10 y 500
    (random() * 100)::int,                        -- Stock aleatorio entre 0 y 100
    (SELECT id FROM users LIMIT 1)                -- Relacionado al primer usuario encontrado
FROM generate_series(1, 10) AS gs;


