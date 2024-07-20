INSERT INTO users(name, email, password) values('modric', 'modric@gmail.com', 'asdf1234');
INSERT INTO users(name, email, password, role) values('toni', 'toni@gmail.com', 'asdf1234', 'ADMIN');

INSERT INTO reservation_time(start_at) values('12:00');
INSERT INTO reservation_time(start_at) values('13:00');
INSERT INTO reservation_time(start_at) values('14:00');

INSERT INTO theme(name, description, thumbnail) values('THE HOLE',
 '강원도 깊은 산골에서 캠핑을 하던중 사라진 일행을 찾기 위해 주변을 찾아다니다가 움푹 꺼진 땅굴을 발견하고 혹시나 하는 마음에 동굴로 들어간 사람들….순간 번쩍이는 불빛에 기절하고 마는데..불빛이 사라지고 겨우 정신을 차리고 눈을 떠 주위를 살펴보니 여기는 동굴이 아니다....',
  '???????????')

--INSERT INTO reservation(name, date, time_id, theme_id) values('modric', '2024-12-31', 1, 1);

