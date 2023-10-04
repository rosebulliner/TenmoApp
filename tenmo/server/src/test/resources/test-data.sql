rollback;
BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id CASCADE;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL DEFAULT 1000, --added this!
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);


CREATE SEQUENCE seq_transfer_id

  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;
  
  CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	amount numeric (13, 2) Not NULL,
	datetime_requested TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	datetime_executed TIMESTAMP,
	status varchar(8) NOT NULL DEFAULT 'Approved' CHECK(status IN('Pending', 'Approved', 'Rejected')),
	sender_id int,
	recipient_id int,
	sender_username varchar(50) NOT NULL,
	recipient_username varchar(50) NOT NULL,  
	CONSTRAINT fk_sender_username FOREIGN KEY (sender_username) REFERENCES tenmo_user (username),
	CONSTRAINT fk_recipient_username FOREIGN KEY (recipient_username) REFERENCES tenmo_user (username)
-- 	CONSTRAINT fk_sender_account_id FOREIGN KEY (sender_id) REFERENCES account (account_id),
-- 	CONSTRAINT fk_recipient_account_id FOREIGN KEY (recipient_id) REFERENCES account (account_id)
);

INSERT INTO tenmo_user(username, password_hash)
VALUES ( 'TheBojackHorseman' , '$2a$10$AsrGWPau8byLcWqquZ7OhOuBUOkLu8nJRBkwCHB9wJDvlEYzUJEC6');
--password is horsin_around
INSERT INTO tenmo_user( username, password_hash)
VALUES ('chavez_todd', '$2a$10$LKUO9UXlKLJD6rquhOo.C.8TmyLwhDPG6oZeHuiFhLHnSdemJTvmm');
-- password is password
INSERT INTO tenmo_user( username, password_hash)
VALUES ('DianeNguyen', '$2a$10$Ppfjxcl.f0EC.3pmLXyoMuD.lQX.a8h.3oh.ADAfh/SVzem6RzTBa');
-- feminism2023!
INSERT INTO tenmo_user( username, password_hash)
VALUES ('Mr_Peanutbutter', '$2a$10$6GOsB/bFx7Jqyr5.8WcAuOWe1oN7Y/Hk9yU6M3n.y/DUuEcsKzL.6');
-- nihilist
INSERT INTO tenmo_user( username, password_hash)
VALUES ('actualSarahLynn', '$2a$10$pjH.UmylVl2ATgrA2LtjCe3VaVXR5/81ERMdqGpCLd36PO6AnVXBG');
-- LIV34EVER

INSERT INTO tenmo_user (username, password_hash)
VALUES ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy');

INSERT INTO account (user_id, balance)
VALUES ((SELECT user_id FROM tenmo_user WHERE username = 'bob'), 1000),
       ((SELECT user_id FROM tenmo_user WHERE username = 'user'), 1000);
	   
INSERT INTO transfer(amount, datetime_requested, status, sender_id, recipient_id, sender_username, recipient_username)
VALUES (489.80, '2023-04-27 02:22:22',  'Rejected', 1001, 1002, 'TheBojackHorseman', 'chavez_todd' ),
(241.69, '2023-02-12 01:05:42', 'Pending', 1005, 1001, 'actualSarahLynn', 'TheBojackHorseman'),
(689.75, '2023-06-15 16:20:59', 'Approved', 1004, 1003, 'Mr_Peanutbutter', 'DianeNguyen'),
(444.12, '2023-01-11 11:11:27', 'Pending', 1006, 1007, 'bob', 'user');


INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'TheBojackHorseman'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'chavez_todd'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'DianeNguyen'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'Mr_Peanutbutter'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'actualSarahLynn'));



UPDATE account SET balance= 310.25 WHERE user_id = 1005; 

UPDATE account SET balance= 1689.75 WHERE user_id = 1004;

UPDATE transfer SET datetime_executed = '2023-06-15 04:35:22'
where transfer_id = 3003;

--SELECT * FROM tenmo_user LEFT JOIN account USING (user_id) LEFT JOIN transfer ON (username=sender_username);


--SELECT * from transfer;


COMMIT;