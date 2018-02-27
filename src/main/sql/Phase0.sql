IF db_id('LS2') IS NULL CREATE DATABASE LS2;

GO
USE LS2

IF object_id('Students') IS NOT NULL
DROP TABLE Students

CREATE TABLE Students
(
name varchar(50) not null,
number int,
primary key(number)
)