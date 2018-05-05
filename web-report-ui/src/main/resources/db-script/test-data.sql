insert into roles(id, role_name) values(1, 'USER');
insert into roles(id, role_name) values(2, 'ADMIN');
insert into roles(id, role_name) values(3, 'REPORT_MANAGER');

insert into users(id, first_name, last_name, email, password, role_id, enabled)
	values(1, 'toyin', 'dipo', 'toyin@mail.com', '873f53f70614b18fc02c50b6ff613e7dc435c8cf320f492d33a928b1279165d29962dc11b1b03312', 2, 1);