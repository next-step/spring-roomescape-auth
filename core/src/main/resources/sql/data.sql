/**
  INSERT users
 */
INSERT INTO users(role, name, email, password)
VALUES ('ADMIN', 'admin', 'admin@email.com', '1234');

INSERT INTO users(role, name, email, password)
VALUES ('CUSTOMER', 'customer', 'customer@email.com', '1234');

/**
  INSERT reservation_times
 */
INSERT INTO reservation_times (time_id, start_at, created_at)
VALUES (300, '10:00:00', '2024-06-25T10:15:30');

INSERT INTO reservation_times (time_id, start_at, created_at)
VALUES (400, '11:00:00', '2024-06-25T10:15:30');


/**
  INSERT themes
 */
INSERT INTO themes(theme_id, name, description, thumbnail, deleted)
values (101, '테마1', 'theme1_descprition', 'https://thumbnail.theme1.com', false);

INSERT INTO themes(theme_id, name, description, thumbnail, deleted)
values (102, '테마2', 'theme2_descprition', 'https://thumbnail.theme2.com', false);

INSERT INTO themes(theme_id, name, description, thumbnail, deleted)
values (103, '테마3', 'theme3_descprition', 'https://thumbnail.theme3.com', false);

/**
  INSERT reservations
 */
INSERT INTO reservations(name, date, time_id, theme_id, status, reserved_at, canceled_at)
VALUES ('name1', '2025-01-29', 300, 101, 'CONFIRMED', '2024-06-25T10:15:31', '2024-06-25T10:15:30');

INSERT INTO reservations(name, date, time_id, theme_id, status, reserved_at, canceled_at)
VALUES ('name2', '2024-06-25', 300, 101, 'CANCELED', '2024-06-25T10:15:31', '2024-06-30T10:15:30');
