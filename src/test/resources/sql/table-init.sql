CREATE SCHEMA IF NOT EXISTS test;

create table if not exists test.users(
    id bigserial primary key,
    email varchar,
    phone_number varchar,
    password varchar,
    role_enum_list varchar[]
);

create table if not exists test.city(
    id bigserial primary key,
    is_visible boolean,
    name varchar,
    tempc double precision
);