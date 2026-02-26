

create schema if not exists tenants;
drop table if exists tenants.tenants cascade;
create table tenants.tenants (
    id serial primary key,
    name varchar(100) unique not null,
    schema_name varchar(100) unique not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP
);

