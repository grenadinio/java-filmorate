package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilmRowMapper implements RowMapper<Film> {

    private final Map<Long, Film> map = new HashMap<>();

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        Film film = map.get(id);

        if (film == null) {
            MPA mpa = new MPA();
            mpa.setId(rs.getLong("mpaRatingId"));
            mpa.setName(rs.getString("mpaRatingName"));
            film = new Film(
                    id,
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("releaseDate").toLocalDate(),
                    rs.getInt("duration"),
                    mpa,
                    new ArrayList<>()
            );
            map.put(id, film);
        }

        long genreId = rs.getLong("genre_id");
        if (genreId > 0) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(rs.getString("genre_name"));

            film.getGenres().add(genre);
        }

        return film;
    }
}
