--liquibase formatted sql 


--changeset Fidel Zarco:1
drop table if exists users cascade;
create table users(
    id serial primary key, 
    username varchar(100) not null, 
    password varchar(100) not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP,
    deleted_at timestamp with time zone
);


--rollback drop table users; 
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
    status varchar(20) default 'PENDIENTE', 
    
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP,
    deleted_at timestamp with time zone,

    constraint fk_sales_user foreign key (user_id) references users(id)
);
--rollback DROP TABLE sales;