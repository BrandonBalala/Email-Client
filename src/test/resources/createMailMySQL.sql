DROP DATABASE IF EXISTS EMAIL_DB;
CREATE DATABASE EMAIL_DB;

USE EMAIL_DB;

DROP TABLE IF EXISTS FOLDER;
CREATE TABLE FOLDER(
	id int(10) NOT NULL auto_increment,
	name varchar(50) NOT NULL,
	PRIMARY KEY  (id),
	UNIQUE(name)
)ENGINE=InnoDB;

-- Status 0 = New email --
-- Status 1 = Received email --
DROP TABLE IF EXISTS MAIL;
CREATE TABLE MAIL(
	id int(10) NOT NULL auto_increment,
	fromField varchar(100) NOT NULL,
	subject varchar(100) NOT NULL,
	textMessage varchar(500) DEFAULT "",
	htmlMessage varchar(500) DEFAULT "",
	dateSent timestamp NOT NULL,
	dateReceived timestamp NOT NULL,
	folder int(10) NOT NULL,
	status int(1) NOT NULL,
	PRIMARY KEY  (id),
    FOREIGN KEY (folder) REFERENCES FOLDER(id) ON DELETE CASCADE
)ENGINE=InnoDB;


 --Type: TO, CC, BCC--
DROP TABLE IF EXISTS ADDRESS;
CREATE TABLE ADDRESS(
	id int(10) NOT NULL auto_increment,
	emailid int(10) NOT NULL,
	email varchar(100) NOT NULL,
	typeField char(3) NOT NULL,
	PRIMARY KEY  (id),
	FOREIGN KEY (emailid) REFERENCES MAIL(id) ON DELETE CASCADE
)ENGINE=InnoDB;

--Type: ATT, EMB--
DROP TABLE IF EXISTS ATTACHMENT;
CREATE TABLE ATTACHMENT(
	id int(10) NOT NULL auto_increment,
	emailid int(10) NOT NULL,
	contentId varchar(50) DEFAULT "",
	name varchar(250) NOT NULL,
	size int(100) NOT NULL,
	content mediumblob NOT NULL,
	typeField char(3) NOT NULL,
	PRIMARY KEY  (id),
	FOREIGN KEY (emailid) REFERENCES MAIL(id) ON DELETE CASCADE
)ENGINE=InnoDB;

/**
 * INSERT COUPLE OF ROWS TO THE TABLES!
 * 
 */
 