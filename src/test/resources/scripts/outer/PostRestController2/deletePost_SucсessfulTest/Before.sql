INSERT INTO posts (id, create_date, pic_url, text, title, user_id) VALUES (1, '2011-01-18', 'some_picture', 'some_text', 'some_title', 1);
INSERT INTO posts (id, create_date, pic_url, text, title, user_id) VALUES (2, '2011-01-18', 'some_picture', 'some_text', 'some_title', 1);
INSERT INTO posts (id, create_date, pic_url, text, title, user_id) VALUES (3, '2011-01-18', 'some_picture', 'some_text', 'some_title', 2);
INSERT INTO posts_tags (post_id, tag) VALUES (1, 's1');
INSERT INTO posts_tags (post_id, tag) VALUES (2, 's2');
INSERT INTO posts_tags (post_id, tag) VALUES (1, 's3');
INSERT INTO posts_tags (post_id, tag) VALUES (3, 's2');
INSERT INTO posts_tags (post_id, tag) VALUES (3, 's3');