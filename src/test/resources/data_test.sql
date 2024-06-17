-- This is an empty data file to prevent data initialization
INSERT INTO member (name, email, password)
VALUES  ('테스트_마스터', 'master@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (name, member_id)
VALUES  ('ADMIN', 1);
