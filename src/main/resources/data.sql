insert into role (id, name) VALUES (1, 'MEMBER');
insert into role (id, name) VALUES (2, 'ADMIN');

--- 관리자 계정(id: admin@email.com / password: 1234)
insert into member (id, email, name, password, role_id) VALUES (9999 ,'admin@email.com', '관리자', '$2a$10$HGgzwqW6INvWjAfhRSQwR.fbwnfvgDlz8BPeMAepj9BUNyJO3Eu.a', 2);

--- 사용자 계정(id: member@email.com / password: 1234)
insert into member (id, email, name, password, role_id) VALUES (10000 ,'member@email.com', '사용자', '$2a$10$HGgzwqW6INvWjAfhRSQwR.fbwnfvgDlz8BPeMAepj9BUNyJO3Eu.a', 1);

INSERT INTO reservation_time (id, start_at) VALUES (8, '18:00');
INSERT INTO reservation_time (id, start_at) VALUES (9, '19:00');
INSERT INTO reservation_time (id, start_at) VALUES (10, '20:00');

INSERT INTO theme (id, name, description, thumbnail) VALUES (8, '테마8', '테마8의 설명입니다', 'samle_thumbnail8');
INSERT INTO theme (id, name, description, thumbnail) VALUES (9, '테마9', '테마9의 설명입니다', 'samle_thumbnail9');
INSERT INTO theme (id, name, description, thumbnail) VALUES (10, '테마10', '테마10의 설명입니다', 'samle_thumbnail10');
