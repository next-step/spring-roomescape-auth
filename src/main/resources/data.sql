INSERT INTO users (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
                                                 ('유저1', 'user@email.com', 'password', 'USER');

INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 1', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 2', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 3', 'Description 1', 'thumbnail1.jpg');

INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('어드민', DATEADD('DAY', 1, CURRENT_DATE), 1, 1);
INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('어드민', DATEADD('DAY', 1, CURRENT_DATE), 1, 2);
