create table room
(
    room_no                  varchar(3) not null,
    bathroom_type            boolean,
    description              varchar(255),
    is_balcony               boolean,
    is_coffee_machine        boolean,
    is_outstanding_view      boolean,
    is_rest_area             boolean,
    is_tv                    boolean,
    created_at               timestamp,
    modified_at              timestamp,
    version                  int4       not null,
    no_people                int4       not null,
    price_per_night_currency varchar(255),
    price_per_night_value    numeric(19, 2),
    room_size_currency       varchar(255),
    room_size_value          numeric(19, 2),
    room_type                int4,
    status                   int4,
    primary key (room_no)
)
