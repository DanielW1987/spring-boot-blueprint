create table users (
    id VARCHAR(255) not null,
    created_at timestamp,
    updated_at timestamp,
    account_expired boolean,
    account_locked boolean,
    credentials_expired boolean,
    email varchar(50) not null,
    enabled boolean,
    password varchar(120) not null,
    primary key (id)
);

create table users_roles (
    users_id VARCHAR(255) not null,
    user_roles varchar(255) not null,
    primary key (users_id, user_roles)
);

alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table users_roles add constraint FKml90kef4w2jy7oxyqv742tsfc foreign key (users_id) references users;
