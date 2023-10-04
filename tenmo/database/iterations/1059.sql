BEGIN TRANSACTION;
DROP TABLE IF EXISTS users CASCADE;
DROP SEQUENCE IF EXISTS seq_user_id CASCADE;

-- Sequence to start user_id values at 301
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 301
  NO MAXVALUE;
--user table creation
CREATE TABLE users (
    user_id int NOT NULL DEFAULT
nextval('seq_user_id'),
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(200) NOT NULL,
    role varchar(50) NOT NULL,
    email varchar(75) NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);
-- Sequence to start list_id values at 401
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 401
  NO MAXVALUE;
  
CREATE TABLE list (
	list_id int NOT NULL DEFAULT)
nextval('seq_list_id'),
	user_id int,
	group_id int,
	item_id int,
	list_name varchar(50) NOT NULL,
	CONSTRAINT 

INSERT INTO users (username,password_hash,role,email) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER', 'user@email.com');
INSERT INTO users (username,password_hash,role,email) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN', 'admin@email.com');
COMMIT TRANSACTION;
select * from users;