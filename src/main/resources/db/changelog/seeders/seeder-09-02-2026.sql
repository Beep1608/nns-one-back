-- 1. insertar el tenant en la tabla maestra (especificando el esquema)
insert into tenants.tenants (email,password,name, schema_name,business_code) 
values ('hola@example.com','12345','tenant 1', 'tenant1','12345') 
on conflict do nothing;

-- 2. ejecutar la función que crea todo el esquema, tablas, y los roles/permisos base
select tenants.create_tenant('tenant1');

-- 3. cambiar el contexto para que los siguientes inserts caigan en el cliente
set search_path to tenant1;

-- 4. insertar usuarios
insert into users (username, password, last_active_at)
select 
    'user_' || gs, 
    '$2a$10$8.unvug9hhgffudalk8qfouvgkqrzkvymge07xd00dmxs.zubzb1a',
    now() - (random() * interval '30 days')
from generate_series(1, 20) as gs
where not exists (
    select 1 from users where username = 'user_' || gs
);

-- 5. asignar el rol de usuario a todos (buscando el id por nombre para evitar errores)
insert into user_roles (user_id, role_id)
select u.id, r.id 
from users u, roles r
where r.name = 'user'
and not exists (
    select 1 from user_roles ur where ur.user_id = u.id and ur.role_id = r.id
);

-- 6. insertar productos
insert into products (name, description, price, stock, user_id)
select 
    'producto ' || gs, 
    'esta es la descripción automática para el producto número ' || gs,
    (random() * (500 - 10) + 10)::decimal(12, 2), 
    (random() * 100)::int,                       
    (select id from users order by id asc limit 1)            
from generate_series(1, 10) as gs;

-- 7. insertar ventas (agregué el from generate_series que faltaba)
insert into sales (user_id, total_amount, status)
select 
    (select id from users order by id asc limit 1),
    (random() * (1000 - 20) + 20)::decimal(12,2),
    'completed'
from generate_series(1, 10) as gs;