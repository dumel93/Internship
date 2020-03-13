insert into departments(name,city) values ('it','rzeszow');
insert into departments(name,city) values ('hr','rzeszow');

insert into users(first_name,last_name,email,password,salary,department_id) values ('admin','admin','ceo@pgs.com','admin123',2000,null);
insert into users(first_name,last_name,email,password,salary,department_id) values ('damian','krawczyk','222@wp.pl','user123',2000,1);
insert into users(first_name,last_name,email,password,salary,is_head,department_id) values ('adrian','strzepek','333@wp.pl','user123',3000,true,1);

insert into roles(role_id,name) values (1,'ROLE_CEO');
insert into user_role values (1,1);

