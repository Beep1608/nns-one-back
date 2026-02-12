--formatted liquibase sql



-- Insertamos 10 usuarios de prueba
INSERT INTO users (username, password, last_active_at)
SELECT 
    'user_' || gs, 
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.zUbZb1a',
    -- Genera una fecha aleatoria entre hoy y hace 30 días
    NOW() - (random() * interval '30 days')
FROM generate_series(1, 10) AS gs
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'user_' || gs
);

-- Insertar roles iniciales
insert into roles (name) values ('admin'), ('user');

-- Asignar ROLE_USER (id: 2) a todos los usuarios creados
INSERT INTO user_roles (user_id, role_id)
SELECT id, 2 FROM users
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles WHERE user_id = users.id
);

insert into permissions (name) values ('read'), ('write'), ('delete');

insert into role_permissions (role_id, permission_id) 
select 1, id from permissions;

--Insertamos 10 productos de prueba 
INSERT INTO products (name, description, price, stock, user_id)
SELECT 
    'Producto ' || gs, 
    'Esta es la descripción automática para el producto número ' || gs,
    (random() * (500 - 10) + 10)::decimal(12, 2), -- Precio aleatorio entre 10 y 500
    (random() * 100)::int,                        -- Stock aleatorio entre 0 y 100
    (SELECT id FROM users LIMIT 1)                -- Relacionado al primer usuario encontrado
FROM generate_series(1, 10) AS gs;


