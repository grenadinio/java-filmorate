package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaStorage implements MpaStorage {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<MPA> get(Long id) {
        String sql = "SELECT * FROM mpa_rating WHERE id = :id";
        List<MPA> mpaList = jdbc.query(sql, Map.of("id", id), new MpaRowMapper());
        return mpaList.stream().findFirst();
    }

    @Override
    public List<MPA> getAll() {
        return jdbc.query("SELECT * FROM mpa_rating", new MpaRowMapper());
    }
}
