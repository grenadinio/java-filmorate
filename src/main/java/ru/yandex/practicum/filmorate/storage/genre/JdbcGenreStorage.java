package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Genre> get(Long id) {
        String sql = "SELECT * FROM genres WHERE id = :id";
        List<Genre> genres = jdbc.query(sql, Map.of("id", id), new GenreRowMapper());
        return genres.stream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("SELECT * FROM genres", new GenreRowMapper());
    }
}
