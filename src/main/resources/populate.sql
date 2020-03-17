insert into departments(name,city,min_salary,max_salary) values ('it','rzeszow',1000,5000);
insert into departments(name,city,min_salary,max_salary) values ('hr','rzeszow',1000,5000);

insert into users(first_name,last_name,email,password,salary,active,is_head,department_id) values ('admin','admin','ceo@pgs.com','admin123',20000,true,false,null);
insert into users(first_name,last_name,email,password,salary,active,is_head,department_id) values ('damian','krawczyk','damian@pgs.com','user123',2000,true,false,1);
insert into users(first_name,last_name,email,password,salary,active,is_head,department_id) values ('adrian','strzepek','adrian@pgs.com','user123',3000,true,true,1);

insert into roles(role_id,name) values (1,'ROLE_CEO');
insert into roles(role_id,name) values (2,'ROLE_HEAD');
insert into roles(role_id,name) values (3,'ROLE_EMPLOYEE');
insert into user_role values (1,1);
insert into user_role values (2,3);
insert into user_role values (3,2);

