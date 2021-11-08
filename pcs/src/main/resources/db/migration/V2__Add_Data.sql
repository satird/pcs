insert into users (active, email, name, password)
values (true, 'satird@mail.ru', 'name', '123'),
       (true, 'satirdima@mail.ru', 'name2', '123'),
       (true, 'satird@gmail.ru', 'sd', '123'),
       (true, 'satirdima@gmail.com', 'name3', '123'),
       (true, 'satirdk@mail.ru', 'name4', '123'),
       (true, 'my@mail.ru', 'admin', '123'),
       (true, 'you@mail.ru', 'root', '123'),
       (true, 'user@mail.ru', 'user', '123');

INSERT INTO roles(name)
VALUES ('ROLE_USER');
INSERT INTO roles(name)
VALUES ('ROLE_MODERATOR');
INSERT INTO roles(name)
VALUES ('ROLE_ADMIN');

insert into user_roles (user_id, role_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (7, 3),
       (6, 3),
       (8, 1);

insert into ad (category, creation_date, premium, text, title, price, user_id)
values (1, '2021-10-15', false, 'Продам велосипед в замечательном состоянии', 'велосипед', 100, 1),
       (5, '2021-10-16', false, 'Продам коньки в замечательном состоянии', 'коньки', 25.5, 4),
       (2, '2021-10-29', true, 'Продам Секрет в замечательном состоянии', 'книга секрет', 10.3, 2),
       (3, '2021-11-01', false, 'Продам тостер в замечательном состоянии', 'тостер', 20, 6),
       (7, '2021-11-02', false, 'Продам джинсы - варёнки в замечательном состоянии', 'джинсы', 50.8, 5),
       (8, '2021-10-15', false, 'Продам матрас в замечательном состоянии', 'матрас', 150, 4),
       (3, '2021-11-06', false, 'Продам лампу в замечательном состоянии', 'лампа', 5, 1),
       (1, '2021-11-03', true, 'Продам тесла немного б/у, батарея держит полдня', 'тесла', 100500, 3),
       (0, '2021-10-30', false, 'Продам комнату новую, на этапе строительства котлован', 'комната', 49999.99, 6),
       (9, '2021-10-25', false, 'Продам слипоны Vans в замечательном состоянииб почти белые', 'Слипоны Vans', 80.5, 8);

insert into comment (creation_date, text, ad_id, user_id)
values ('2021-10-25', 'привет, интересный товар', 1, 1),
       ('2021-10-26', 'привет, за полцены заберу сегодня', 2, 2),
       ('2021-10-30', 'здравствуйте, а бесплатно можно, у вас карма поднимется)', 3, 3),
       ('2021-11-02', 'привет, очень дорого', 4, 4),
       ('2021-10-26', 'здравствуйте, гавно товар, у меня такой был не берете', 1, 5),
       ('2021-10-30', 'привет, заходите у меня ноготочки!', 3, 6),
       ('2021-09-05', 'здравствуйте, а плинтуса не продаёте? на фотке видно', 2, 4),
       ('2021-10-16', 'привет, буржую, лучше бы в детдом отдали!', 1, 2),
       ('2021-11-05', 'привет, не покупайте у него ничего он - спекулянт!', 4, 3),
       ('2021-10-25', 'здравствуйте, а если брак? гарантия есть?', 6, 5),
       ('2021-10-17', 'привет, продаван!!! Почему не отвечаешь на комменты????', 1, 1);

insert into message (chat_id, creation_date, message_status, text, message_to, message_from)
values ('7_1', '2021-10-16', 1, 'Отдай за так!', 1, 7),
       ('7_1', '2021-10-16', 1, 'а за воротник не надо?', 7, 1),
       ('7_1', '2021-10-16', 1, 'зачем грубить?', 1, 7),
       ('7_1', '2021-10-16', 1, 'я не грублю, просто спросил)', 7, 1),
       ('8_3', '2021-10-16', 1, 'придержите, пожалуйста, я сейчас на программиста выучусь, заработаю и у вас сраз заберу. Года через два', 3,
        8),
       ('8_3', '2021-10-16', 1, 'красивое...', 8, 3);

insert into chat_room(chat_id, recipient, sender, ad_id)
values ('7_1', 1, 7, 1),
       ('7_1', 7, 1, 1),
       ('8_3', 3, 8, 8),
       ('8_3', 8, 3, 8);
insert into rating(score, user_id)
values (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 6),
       (5, 7),
       (5, 8),
       (3, 1),
       (2, 2),
       (4, 3),
       (5, 6),
       (4, 6),
       (1, 7),
       (4, 8);
