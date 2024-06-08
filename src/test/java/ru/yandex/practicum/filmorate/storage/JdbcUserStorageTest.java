package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.JdbcUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcUserStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcUserStorageTest {
    private static final String TEST_USER_EMAIL = "test1@example.com";
    private static final long TEST_USER_ID = 1L;
    private final JdbcUserStorage userStorage;

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.get(TEST_USER_ID);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", TEST_USER_ID);
                            assertThat(user).hasFieldOrPropertyWithValue("email", TEST_USER_EMAIL);
                        }
                );
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userStorage.getAll();
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(3);
    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        newUser.setEmail("new@mail.com");
        newUser.setLogin("newUser");
        newUser.setName("New User");
        newUser.setBirthday(LocalDate.now());

        User createdUser = userStorage.create(newUser);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
    }

    @Test
    public void testUpdateUser() {
        Optional<User> userOptional = userStorage.get(TEST_USER_ID);
        User user = userOptional.orElseThrow();
        user.setName("Updated Name");

        User updatedUser = userStorage.update(user);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testAddAndDeleteFriend() {
        userStorage.addFriend(TEST_USER_ID, 2L);
        List<User> friends = userStorage.getFriends(TEST_USER_ID);
        assertThat(friends).isNotEmpty();

        userStorage.deleteFriend(TEST_USER_ID, 2L);
        friends = userStorage.getFriends(TEST_USER_ID);
        assertThat(friends).isEmpty();
    }

    @Test
    public void testGetCommonFriends() {
        userStorage.addFriend(TEST_USER_ID, 2L);
        userStorage.addFriend(TEST_USER_ID, 3L);
        userStorage.addFriend(2L, 3L);

        List<User> commonFriends = userStorage.getCommonFriends(TEST_USER_ID, 2L);
        assertThat(commonFriends).isNotEmpty();
        assertThat(commonFriends).hasSize(1);
    }
}
