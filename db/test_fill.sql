
--
-- Cleanup
--

delete from users where id >= 2;

delete from surveys_users;
delete from surveys_text;
delete from survey_stats;
delete from surveys;


SELECT SHA2('bagel', 256) INTO @pw;

INSERT INTO users (`id`, `email`, `full_name`, `users_name`, `password`, `blocked`, `role`, `company`) VALUES

  (2, 'username-2@example.com', 'Менеджер Первый', 'manager1', @pw, FALSE, 'manager', 'Рога и копыта'),
  (3, 'username-3@example.com', 'Менеджер Второй', 'manager2', @pw, FALSE, 'manager', 'Рога и копыта'),
  (4, 'username-4@example.com', 'Менеджер Третий', 'manager3', @pw, FALSE, 'manager', null),

  (5,  'username-5@example.com',  'Заказчик Иванович Первый',    'client1', @pw, FALSE, 'client', 'Хвост и хобот'),
  (6,  'username-6@example.com',  'Заказчик Иванович Второй',    'client2', @pw, FALSE, 'client', 'Хвост и хобот'),
  (7,  'username-7@example.com',  'Заказчик Иванович Третий',    'client3', @pw, FALSE, 'client', 'Рога и копыта'),
  (8,  'username-8@example.com',  'Заказчик Иванович Четвертый', 'client4', @pw, FALSE, 'client', 'Хвост и хобот'),
  (9,  'username-9@example.com',  'Заказчик Иванович Пятый',     'client5', @pw, FALSE, 'client', null),
  (10, 'username-10@example.com', 'Заказчик Иванович Шестой',    'client6', @pw, FALSE, 'client', null);


INSERT INTO surveys (id, active, startdate, expires) VALUES
  (1, TRUE,   '2014-01-01 12:15:00', '2014-01-01 13:15:00'),
  (2, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (3, TRUE,   '2012-02-10 14:00:00', '2015-03-10 14:00:00'),
  (4, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (5, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (6, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (7, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (8, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (9, TRUE,   '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (10, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (11, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (12, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (13, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (14, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00'),
  (15, TRUE,  '2014-02-10 14:00:00', '2014-03-10 14:00:00');


INSERT INTO surveys_text (survey_id, title) VALUES
  (1,   'Опрос 1'),
  (2,   'Опрос 2'),
  (3,   'Опрос 3'),
  (4,   'Опрос 4'),
  (5,   'Опрос 5'),
  (6,   'Опрос 6'),
  (7,   'Опрос 7'),
  (8,   'Опрос 8'),
  (9,   'Опрос 9'),
  (10,  'Опрос 10'),
  (11,  'Опрос 11'),
  (12,  'Опрос 12'),
  (13,  'Опрос 13'),
  (14,  'Опрос 14'),
  (15,  'Опрос 15');


INSERT INTO surveys_users (survey_id, user_id) VALUES
  (1,   5),
  (2,   5),
  (3,   5),
  (4,   5),
  (5,   5),
  (6,   5),
  (7,   5),
  (8,   5),
  (9,   5),
  (10,  5),
  (11,  5),
  (12,  5),
  (13,  5),
  (14,  5),
  (15,  6);


INSERT INTO survey_stats (survey_id, accessNumber) VALUES
  (1, NULL),
  (2, NULL),
  (3, NULL),
  (4, NULL),
  (5, NULL),
  (6, NULL),
  (7, NULL),
  (8, '+44-123-456-78'),
  (9, NULL),
  (10, NULL),
  (11, NULL),
  (12, NULL),
  (13, '*123#'),
  (14, NULL),
  (15, NULL);


insert into questions(id, survey_id, title, question_order) values
  (1, 1, 'Please select your age', 0),
  (2, 1, 'Please select your occupation', 1),
  (3, 1, 'What is your favorite color?', 2);


insert into question_options(question_id, code, answer) values
  (1, 'O1', '<18'),
  (1, 'O2', '18-30'),
  (1, 'O3', '>30'),

  (2, 'O1', 'Unemployed'),
  (2, 'O2', 'Own business'),
  (2, 'O3', 'Other'),

  (3, 'O1', 'Reg'),
  (3, 'O2', 'Orange'),
  (3, 'O3', 'Yellow'),
  (3, 'O4', 'Green'),
  (3, 'O5', 'Blue'),
  (3, 'O6', 'Violet');
