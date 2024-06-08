merge into MPA_RATING (ID, NAME)
    values (1, 'G');
merge into MPA_RATING (ID, NAME)
    values (2, 'PG');
merge into MPA_RATING (ID, NAME)
    values (3, 'PG-13');
merge into MPA_RATING (ID, NAME)
    values (4, 'R');
merge into MPA_RATING (ID, NAME)
    values (5, 'NC-17');

merge into GENRES (ID, NAME)
    values (1, 'Комедия');
merge into GENRES (ID, NAME)
    values (2, 'Драма');
merge into GENRES (ID, NAME)
    values (3, 'Мультфильм');
merge into GENRES (ID, NAME)
    values (4, 'Триллер');
merge into GENRES (ID, NAME)
    values (5, 'Документальный');
merge into GENRES (ID, NAME)
    values (6, 'Боевик');

insert into USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('test1@example.com', 'test1', 'Test User 1', '2001-01-01'),
       ('test2@example.com', 'test2', 'Test User 2', '2002-02-02'),
       ('test3@example.com', 'test3', 'Test User 3', '2003-03-03');

insert into FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPARATINGID)
values ('TestFilm1', 'Testdecription1.', '2001-01-01', 120, 1),
       ('TestFilm2', 'Testdecription2.', '2002-02-02', 90, 2);

insert into FILM_GENRE (FILMID, GENREID)
values (1, 1),
       (2, 2);