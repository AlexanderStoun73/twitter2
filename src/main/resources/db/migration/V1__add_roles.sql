insert into usr (id, username, password, isactive)
values (1, 'admin', '123', true);

insert into user_role (user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN');