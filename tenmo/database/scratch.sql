SELECT * FROM transfer;

SELECT * FROM tenmo_user;

SELECT * FROM account;

INSERT INTO transfer (amount, sender_id, recipient_id)
VALUES (25, 2002, 2003);

INSERT INTO transfer (amount, sender_id, recipient_id)
VALUES (25, 2003, 2005);

UPDATE transfer SET status = 'Approved';


UPDATE account SET balance = 10000;