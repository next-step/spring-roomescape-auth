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
VALUES  ('관리자', 'tester@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (name, member_id)
VALUES  ('ADMIN', 1);

INSERT INTO member (name, email, password)
VALUES  ('신규_유저', 'newbie@gmail.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

INSERT INTO member_role (name, member_id)
VALUES  ('GUEST', 2);
