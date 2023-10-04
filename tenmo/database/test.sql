INSERT into tenmo_user(username, password_hash)
VALUES( 'doe_j', 'password1');
INSERT INTO tenmo_user(username, password_hash)
VALUES ('snailDad', 'GarySquarePant$');
INSERT INTO tenmo_user( username, password_hash)
VALUES ('texas_yellow_rose', 'SVndyCh33ks');

INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'doe_j'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'snailDad'));
INSERT INTO account (user_id)
VALUES((SELECT user_id FROM tenmo_user WHERE username = 'texas_yellow_rose'));







