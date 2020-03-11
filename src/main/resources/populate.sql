SET FOREIGN_KEY_CHECKS = 0;

drop table if exists DEPARTMENTS;
drop table if exists USERS;
drop table if exists ROLES;
drop table if exists USER_ROLE;
--
-- TRUNCATE TABLE departments   TO NIE DZIALA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- TRUNCATE TABLE users

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE departments (
  department_id bigint NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  city varchar(50) DEFAULT NULL,
  min_salary decimal(10,0) DEFAULT NULL,
  max_salary decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (department_id)
);
CREATE TABLE  users (
  user_id bigint NOT NULL AUTO_INCREMENT,
  first_name varchar(100) DEFAULT NULL,
  last_name varchar(100) DEFAULT NULL,
  password varchar(100) DEFAULT NULL,
  email varchar(50) NOT NULL,
  active bit(1) DEFAULT 0 ,
  department_id bigint DEFAULT NULL,
  is_head bit(1) DEFAULT 0 ,
  salary decimal(10,0) DEFAULT NULL,
  created_date date DEFAULT NULL,
  last_login_time datetime DEFAULT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY email (email),
  FOREIGN KEY (department_id) REFERENCES departments (department_id)
);

CREATE TABLE roles (
  role_id bigint NOT NULL AUTO_INCREMENT,
  name varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
);

CREATE TABLE user_role (
  role_id bigint NOT NULL,
  user_id bigint NOT NULL,
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
);


insert into departments(name,city) values ('it','rzeszow');
insert into departments(name,city) values ('hr','rzeszow');

insert into users(first_name,last_name,email,password,salary) values ('admin','admin','ceo@pgs.com','admin123',2000);
insert into users(first_name,last_name,email,password,salary,department_id) values ('damian','krawczyk','222@wp.pl','user123',2000,1);
insert into users(first_name,last_name,email,password,salary,is_head,department_id) values ('adrian','strzepek','333@wp.pl','user123',3000,true,1);
insert into roles(role_id,name) values (1,'ROLE_CEO');
insert into user_role values (1,1);

