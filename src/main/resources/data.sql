INSERT INTO users (name, email, password, role) VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
                                                 ('유저1', 'user@email.com', 'password', 'USER');

INSERT INTO reservation_time (start_at) VALUES ('10:00');
INSERT INTO reservation_time (start_at) VALUES ('11:00');
INSERT INTO reservation_time (start_at) VALUES ('12:00');

INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 1', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 2', 'Description 1', 'thumbnail1.jpg');
INSERT INTO theme (name, description, thumbnail) VALUES ('Theme 3', 'Description 1', 'thumbnail1.jpg');

INSERT INTO reservation (date, user_id, time_id, theme_id) VALUES ('2024-06-23', 1, 1, 1);
INSERT INTO reservation (date, user_id, time_id, theme_id) VALUES ('2024-06-24', 1, 1, 2);
