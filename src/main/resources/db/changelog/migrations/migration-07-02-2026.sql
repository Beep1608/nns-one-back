--liquibase formatted sql 


--changeset Fidel Zarco:1
drop table if exists users cascade;
create table users(
    id serial primary key, 
    username varchar(100) not null, 
    password varchar(100) not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP,
    last_active_at timestamp with time zone,
    deleted_at timestamp with time zone
);
--rollback drop table users; 

drop table if exists roles cascade;
create table roles(
    id serial primary key,
    name varchar(50) unique not null 
);
--rollback drop table roles; 

drop table if exists user_roles cascade;
create table user_roles(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint fk_user foreign key(user_id) references users(id) on delete cascade,
    constraint fk_role foreign key(role_id) references roles(id) on delete cascade
);
--rollback drop table user_roles; 

drop table if exists permissions cascade;
create table permissions(
    id serial primary key,
    name varchar(100) unique not null -- Ej: 'user:read', 'user:write'
);
--rollback drop table permissions; 

drop table if exists role_permissions cascade;
create table role_permissions(
    role_id int not null,
    permission_id int not null,
    primary key (role_id, permission_id),
    constraint fk_role_perm foreign key(role_id) references roles(id) on delete cascade,
    constraint fk_permission_role foreign key(permission_id) references permissions(id) on delete cascade
);
--rollback drop table role_permissions; 

drop table if exists products cascade;
create table products (
    id serial primary key,
    name varchar(150) not null,
    description text,
    price decimal(12, 2) not null default 0.00,
    stock int not null default 0,
    user_id int not null, 
    

    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP,
    deleted_at timestamp with time zone,

    constraint fk_products_user  foreign key (user_id) references users(id) on delete cascade
);
--rollback DROP TABLE products;

drop table if exists sales cascade;
create table sales (
    id serial primary key,
    user_id int not null, 
    total_amount decimal(12, 2) not null default 0.00,
    status varchar(20) default 'PENDING', 
    
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP,
    deleted_at timestamp with time zone,

    constraint fk_sales_user foreign key (user_id) references users(id)
);
--rollback DROP TABLE sales;