IF db_id('LS2') IS NULL CREATE DATABASE LS2;

GO
USE LS2


CREATE TABLE MOVIE (
	mid INT IDENTITY (1,1),
	Title VARCHAR(50) UNIQUE NOT NULL,
	Release_Year INT NOT NULL,
	Duration INT NOT NULL,
	PRIMARY KEY (mid)
);


CREATE TABLE CINEMA (
	cid INT IDENTITY (1,1),
	Name VARCHAR(25) NOT NULL,
	City VARCHAR(25) NOT NULL,
	PRIMARY KEY (cid),
	UNIQUE (Name, City)
);


CREATE TABLE THEATER (
	tid INT IDENTITY (1,1),
	SeatsAvailable INT NOT NULL,
	Rows INT NOT NULL,
	Seats INT NOT NULL,
	Theater_Name VARCHAR(25) NOT NULL,
	cid INT REFERENCES CINEMA(cid),
	PRIMARY KEY (tid)
);


CREATE TABLE CINEMA_SESSION(
	sid INT IDENTITY (1,1),
	Date DATETIME NOT NULL,
	mid INT REFERENCES MOVIE(mid),
	tid INT REFERENCES THEATER(tid),
	SeatsAvailable INT NOT NULL,
	PRIMARY KEY (sid)
);


CREATE TABLE TICKET(
	tkid VARCHAR(3),
	seat INT NOT NULL,
	row CHAR NOT NULL,
	sid INT REFERENCES CINEMA_SESSION(sid),
	PRIMARY KEY (sid, tkid)
);