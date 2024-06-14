INSERT INTO reservation_time (start_at)
VALUES  ('10:00'),
        ('12:00'),
        ('14:00'),
        ('16:00'),
        ('18:00'),
        ('20:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('고양이 테마', '고양이 카페에서 탈출하는 테마입니다', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('쏘우 테마', '심약자 주의! 공포 테마입니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg'),
       ('포켓몬 테마', '포켓몬고를 설치하셔야 합니다.', 'https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg');

INSERT INTO member (name, email, password)
VALUES  ('테스트_유저', 'tester@gmail.com', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b');

INSERT INTO member_role (name, member_id)
VALUES  ('ADMIN', 1);
