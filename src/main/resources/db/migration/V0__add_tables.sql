CREATE TABLE message (
    id int8 not null,
    filename varchar(255),
    tag varchar(255),
    text varchar(2048),
    user_id int8,
    primary key (id)
);

CREATE TABLE user_role (
    user_id int8 not null ,
    roles varchar(255)
);

CREATE TABLE usr (
    id int8 not null ,
    activation_code varchar(255),
    isactive boolean not null ,
    email varchar(255),
    password varchar(255) not null ,
    username varchar(255) not null ,
    primary key (id)
);

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint user_role_user_fk
    foreign key (user_id) references usr;
