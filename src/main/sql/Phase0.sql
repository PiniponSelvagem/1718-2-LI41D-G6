IF db_id('LS2') IS NULL CREATE DATABASE LS2;

GO
USE LS2

IF object_id('students') IS NOT NULL
DROP TABLE students

CREATE TABLE students
(
name varchar(50),
number int not null,
primary key(number)
)

insert into students values ('NomeTeste', 10)

select number from students where name = 'NomeTeste'