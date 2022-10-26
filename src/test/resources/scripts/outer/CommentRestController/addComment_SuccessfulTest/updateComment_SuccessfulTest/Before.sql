INSERT INTO posts(id, created_date, pic_url, text, title, user_id)
VALUES (1, current_date, null, 'qwerty', 'qwer', 1);

INSERT INTO comments (id, created_date, text, user_id, post_id)
VALUES (1, current_date, 'qwerty', 1, 1);