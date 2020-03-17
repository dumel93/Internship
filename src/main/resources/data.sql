insert into users(first_name,last_name,password,email,active,department_id,is_head,salary,created_date) values ('admin','admin','$2a$10$uO.2uAmBfwFT/NVmBQ9sjObeSUMt2Ulm9weQSltF5YonoxcYAdZM2','ceo@pgs.com',true,null,false,20000,now());
insert into roles(role_id,name) values (1,'ROLE_CEO');
insert into roles(role_id,name) values (2,'ROLE_HEAD');
insert into roles(role_id,name) values (3,'ROLE_EMPLOYEE');
insert into user_role values (1,1);
