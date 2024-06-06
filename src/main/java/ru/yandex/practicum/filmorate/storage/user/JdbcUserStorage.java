package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserStorage implements UserStorage {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<User> get(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        List<User> users = jdbc.query(sql, Map.of("id", id), new UserRowMapper());
        return users.stream().findFirst();
    }

    @Override
    public List<User> getAll() {
        return jdbc.query("SELECT * FROM users", new UserRowMapper());
    }

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource(params);
        jdbc.update(sql, parameterSource, keyHolder, new String[]{"ID"});

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users " +
                "SET email = :email, login = :login, name = :name, birthday = :birthday " +
                "WHERE id = :id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("name", user.getName());
        params.put("birthday", user.getBirthday());

        jdbc.update(sql, params);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String checkSql = "SELECT STATUS FROM USER_FRIENDS WHERE USERID = :friendId AND FRIENDID = :userId";
        List<String> statuses = jdbc.queryForList(checkSql, Map.of("userId", userId, "friendId", friendId), String.class);

        if (statuses.isEmpty()) {
            String insertSql = "INSERT INTO USER_FRIENDS (USERID, FRIENDID, STATUS) VALUES (:userId, :friendId, 'unconfirmed')";
            jdbc.update(insertSql, Map.of("userId", userId, "friendId", friendId));
        } else {
            String updateSql = "UPDATE USER_FRIENDS SET STATUS = 'confirmed' WHERE (USERID = :userId AND FRIENDID = :friendId) OR (USERID = :friendId AND FRIENDID = :userId)";
            jdbc.update(updateSql, Map.of("userId", userId, "friendId", friendId));

            String insertSql = "INSERT INTO USER_FRIENDS (USERID, FRIENDID, STATUS) VALUES (:userId, :friendId, 'confirmed')";
            jdbc.update(insertSql, Map.of("userId", friendId, "friendId", userId));
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM USER_FRIENDS WHERE USERID = :userId AND FRIENDID = :friendId";
        jdbc.update(sql, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql = "SELECT u.* FROM USERS u INNER JOIN USER_FRIENDS uf ON u.ID = uf.FRIENDID WHERE uf.USERID = :userId";
        return jdbc.query(sql, Map.of("userId", userId), new UserRowMapper());
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        String sql = "SELECT u.* FROM USERS u " +
                "INNER JOIN USER_FRIENDS uf1 ON u.ID = uf1.FRIENDID " +
                "INNER JOIN USER_FRIENDS uf2 ON u.ID = uf2.FRIENDID " +
                "WHERE uf1.USERID = :userId AND uf2.USERID = :otherId";
        return jdbc.query(sql, Map.of("userId", userId, "otherId", otherId), new UserRowMapper());
    }
}
