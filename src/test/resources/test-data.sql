INSERT INTO reservation_time (start_at)
VALUES ('10:00');
INSERT INTO reservation_time (start_at)
VALUES ('12:00');
INSERT INTO reservation_time (start_at)
VALUES ('14:00');

INSERT INTO theme (name, description, thumbnail)
VALUES ('테마1', '설명1', '썸네일1');
INSERT INTO theme (name, description, thumbnail)
VALUES ('테마2', '설명2', '썸네일2');

INSERT INTO reservation(name, date, time_id, theme_id)
VALUES ('김준성', '2024-12-25', 1, 1);

INSERT INTO member(name, email, password)
VALUES ('제이슨', 'json@email.com', '1234');
