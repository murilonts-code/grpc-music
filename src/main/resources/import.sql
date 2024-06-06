INSERT INTO users (id, name, age)
VALUES (1, 'User1', 10),
       (2, 'User2', 20),
       (3, 'User3', 30),
       (4, 'User4', 40);

INSERT INTO music (id, name, artist)
VALUES (1, 'Shape of You', 'Ed Sheeran'),
       (2, 'Blinding Lights', 'The Weeknd'),
       (3, 'Rolling in the Deep', 'Adele'),
       (4, 'Havana', 'Camila Cabello'),
       (5, 'Uptown Funk', 'Mark Ronson ft. Bruno Mars');

INSERT INTO playlist (id, name, user_id)
VALUES (1, 'My First Playlist', 1),
       (2, 'My second Playlist', 3),
       (3, 'My Third Playlist', 2),
       (4, 'My Fourth Playlist', 1),
       (5, 'My Fifth Playlist', 2);

insert into playlist_music (playlist_id, music_id)
VALUES (1, 1),
       (1, 2),
       (1, 5),
       (2, 2),
       (2, 1),
       (3, 4),
       (3, 5),
       (4, 2),
       (4, 3),
       (4, 5),
       (5, 1),
       (5, 5);
