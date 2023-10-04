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
	CONSTRAINT fk_recipient_username FOREIGN KEY (recipient_username) REFERENCES tenmo_user (username),
	CONSTRAINT fk_sender_account_id FOREIGN KEY (sender_id) REFERENCES account (account_id),
	CONSTRAINT fk_recipient_account_id FOREIGN KEY (recipient_id) REFERENCES account (account_id)
);
  
INSERT INTO tenmo_user(username, password_hash)
VALUES ( 'TheBojackHorseman' , '$2a$10$AsrGWPau8byLcWqquZ7OhOuBUOkLu8nJRBkwCHB9wJDvlEYzUJEC6');
--password is horsin_around
INSERT INTO tenmo_user(username, password_hash)
VALUES ('princess_carolyn_manager', '  $2a$10$Ud5uNeW1p6dyyHmi.nLLIeIoRIsj1UT9mcd2bMeadKqYHqF/NY2Hy');
--password is Philbert23
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
INSERT INTO tenmo_user( username, password_hash)
VALUES ('Margo_Martindale', '$2a$10$U0UnmckdjJGSPLcu.4nAP.6l0hC7w1W.QfouvA0AmOA6FAsev0mVy');
-- CharacterActress.
INSERT INTO tenmo_user( username, password_hash)
VALUES ('JudahMannowdog', '$2a$10$lSEtdP.ybb.wthfGDqcWJ.uD.zCrsUUag5jSP.gb26FqaWhQmXGWW');
-- EF458SGJSI
INSERT INTO tenmo_user( username, password_hash)
VALUES ('TinaBear_Nurse', '$2a$10$E5nV7XlWx1A0DNe4G2nwFu4XeFSsLawa1usr59eHW6RH5g6HVTMta');
-- AlliedHealth8697
INSERT INTO tenmo_user( username, password_hash)
VALUES ('HollyhockMMGRZHFM', '$2a$10$eEHhIxAkQ4LVAr0GCpyjHecvaOvvfSkUf1Xbz4SYfJGMKsKb98Vum');
-- 8Fathers1ME


INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'TheBojackHorseman'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'princess_carolyn_manager'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'chavez_todd'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'DianeNguyen'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'Mr_Peanutbutter'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'actualSarahLynn'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'Margo_Martindale'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'JudahMannowdog'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'TinaBear_Nurse'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'HollyhockMMGRZHFM'));


COMMIT;
