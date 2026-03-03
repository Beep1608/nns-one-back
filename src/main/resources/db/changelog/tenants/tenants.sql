

create schema if not exists tenants;
drop table if exists tenants.tenants cascade;
create table tenants.tenants (
    id serial primary key,
    name varchar(100) unique not null,
    email text unique not null,
    password varchar(100) not null,
    schema_name varchar(100) unique not null,
    business_code text unique not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP
);
