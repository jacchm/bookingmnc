-- create table rooms
-- (
--     room_no                  varchar(255) not null,
--     bathroom_type            int4,
--     description              varchar(255),
--     is_balcony               boolean,
--     is_coffee_machine        boolean,
--     is_outstanding_view      boolean,
--     is_rest_area             boolean,
--     is_tv                    boolean,
--     room_created_at          timestamp,
--     room_modified_at         timestamp,
--     room_version             int4,
--     no_people                int4         not null,
--     price_per_night_currency varchar(255),
--     price_per_night_value    numeric(19, 2),
--     room_size_currency       varchar(255),
--     room_size_value          numeric(19, 2),
--     room_type                int4,
--     status                   int4,
--     primary key (room_no)
-- );
-- create table uris
-- (
--     id      int4 not null,
--     room_no varchar(255),
--     uri     varchar(255),
--     primary key (id)
-- );
-- create table users
-- (
--     username    varchar(255) not null,
--     authorities varchar(255),
--     email       varchar(255),
--     name        varchar(255),
--     password    varchar(255),
--     surname     varchar(255),
--     primary key (username)
-- )

-- insert into rooms (bathroom_type, description, is_balcony, is_coffee_machine, is_outstanding_view, is_rest_area, is_tv,
--                    room_created_at, room_modified_at, room_version, no_people, price_per_night_currency,
--                    price_per_night_value, room_size_currency, room_size_value, room_type, status, room_no)
-- values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
