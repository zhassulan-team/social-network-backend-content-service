INSERT INTO posts(id, created_date, pic_url, text, title, user_id)
VALUES (1, current_date,null,'qwerty','qwer',1);

INSERT INTO comments (id, created_date, text, user_id, post_id)
VALUES (1,current_date,'Black and white show!',3,1),
       (2,current_date,'Could you tell me why you applied for this job?',1,1),
       (3,current_date,'All Right!That’s the word on the street, isn’t it?',3,1),
       (4,current_date,'Did you feel it?',2,1),
       (5,current_date,'Terrific!That’s the word on the street, isn’t it?',5,1),
       (6,current_date,'Right!',4,1)