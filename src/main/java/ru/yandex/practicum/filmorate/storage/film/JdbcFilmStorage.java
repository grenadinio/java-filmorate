package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcFilmStorage implements FilmStorage {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Film> get(Long id) {
        String sql = "SELECT * FROM films WHERE id = :id";
        List<Film> films = jdbc.query(sql, Map.of("id", id), new FilmRowMapper(jdbc));
        return films.stream().findFirst();
    }

    @Override
    public List<Film> getAll() {
        return jdbc.query("SELECT * FROM films", new FilmRowMapper(jdbc));
    }

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpaRatingId) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpaRatingId)";

        HashMap<String, Object> params = new HashMap<>();
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaRatingId", film.getMpa().getId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(params);

        jdbc.update(sql, parameterSource, keyHolder, new String[]{"ID"});
        Long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbc.update("INSERT INTO FILM_GENRE (FILMID, GENREID) VALUES (:film_id, :genre_id)",
                        Map.of("film_id", filmId,
                                "genre_id", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new BeanPropertyRowMapper<>(MPA.class));
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENREID WHERE fg.FILMID = :filmId",
                Map.of("filmId", filmId), new BeanPropertyRowMapper<>(Genre.class));
        film.setGenres(genres);

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films " +
                "SET name = :name, description = :description, releaseDate = :releaseDate, " +
                "duration = :duration, mpaRatingId = :mpaRatingId " +
                "WHERE id = :id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", film.getId());
        params.put("name", film.getName());
        params.put("description", film.getDescription());
        params.put("releaseDate", film.getReleaseDate());
        params.put("duration", film.getDuration());
        params.put("mpaRatingId", film.getMpa().getId());

        jdbc.update(sql, params);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbc.update("DELETE FROM FILM_GENRE WHERE FILMID = :film_id", Map.of("film_id", film.getId()));
                jdbc.update("INSERT INTO FILM_GENRE (FILMID, GENREID) VALUES (:film_id, :genre_id)",
                        Map.of("film_id", film.getId(),
                                "genre_id", genre.getId()));
            }
        }

        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", film.getMpa().getId()), new BeanPropertyRowMapper<>(MPA.class));
        film.setMpa(mpa);

        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENREID WHERE fg.FILMID = :filmId",
                Map.of("filmId", film.getId()), new BeanPropertyRowMapper<>(Genre.class));
        film.setGenres(genres);

        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
        jdbc.update("INSERT INTO user_film_likes (userId, filmId) VALUES (:userId, :filmId)",
                Map.of("userId", userId, "filmId", id));
    }

    @Override
    public void removeLike(Long id, Long userId) {
        jdbc.update("DELETE FROM user_film_likes WHERE userId = :userId AND filmId = :filmId",
                Map.of("userId", userId, "filmId", id));
    }

    @Override
    public List<Film> getTopLikedFilms(Long count) {
        String sql = "SELECT films.*, COUNT(user_film_likes.filmId) as likes " +
                "FROM films " +
                "LEFT JOIN user_film_likes ON films.id = user_film_likes.filmId " +
                "GROUP BY films.id " +
                "ORDER BY likes DESC " +
                "LIMIT :count";
        return jdbc.query(sql, Map.of("count", count), new FilmRowMapper(jdbc));
    }

    public boolean hasUserLikedFilm(Long userId, Long filmId) {
        String sql = "SELECT COUNT(*) FROM user_film_likes WHERE userId = :userId AND filmId = :filmId";
        Integer count = jdbc.queryForObject(sql, Map.of("userId", userId, "filmId", filmId), Integer.class);
        return count != null && count > 0;
    }
}
