package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(rs.getLong("ID"));
        mpa.setName(rs.getString("NAME"));

        return mpa;
    }
}
