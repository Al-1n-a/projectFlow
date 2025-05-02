insert into users (id, archive, email, name, password, role)
values (1, false, 'admin@mail.ru', 'admin', 'pass', 'ADMIN');

alter sequence user_seq restart with 2;