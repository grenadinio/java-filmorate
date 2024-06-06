package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("id");

        // Fetch the MPA rating
        MPA mpa = jdbc.queryForObject("SELECT * FROM MPA_RATING WHERE ID = :mpaRatingId",
                Map.of("mpaRatingId", rs.getLong("mpaRatingId")), new BeanPropertyRowMapper<>(MPA.class));

        // Fetch the genres
        List<Genre> genres = jdbc.query("SELECT g.* FROM GENRES g JOIN FILM_GENRE fg ON g.ID = fg.GENREID WHERE fg.FILMID = :filmId",
                Map.of("filmId", filmId), new BeanPropertyRowMapper<>(Genre.class));

        return new Film(
                filmId,
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                genres
        );
    }
}
