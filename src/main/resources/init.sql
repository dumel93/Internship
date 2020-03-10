drop table if exists DEPARTMENTS;
drop table if exists USERS;


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
  active bit(1) ,
  department_id bigint DEFAULT NULL,
  is_head bit(1) ,
  salary decimal(10,0) DEFAULT NULL,
  created_date date DEFAULT NULL,
  last_login_time datetime DEFAULT NULL,
  PRIMARY KEY (user_id),
  UNIQUE KEY email (email),
  FOREIGN KEY (department_id) REFERENCES departments (department_id)
);

insert into departments(name,city) values ('it','rzeszow');
insert into departments(name,city) values ('hr','rzeszow');

-- insert into users(first_name,last_name,email,password,salary,department_id) values ('admin','admin','ceo@pgs.com','admin123',2000,null);
insert into users(first_name,last_name,email,password,salary,department_id) values ('damian','krawczyk','222@wp.pl','user123',2000,1);
insert into users(first_name,last_name,email,password,salary,is_head,department_id) values ('adrian','strzepek','333@wp.pl','user123',3000,true,1);
-- insert into user_role values (1,1)

