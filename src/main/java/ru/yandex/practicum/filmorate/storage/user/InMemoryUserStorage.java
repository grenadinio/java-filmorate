package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentMaxId = 1;

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User create(User user) {
        user.setId(currentMaxId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        users.get(userId).getFriendsIds().add(friendId);
        users.get(friendId).getFriendsIds().add(userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        users.get(userId).getFriendsIds().remove(friendId);
        users.get(friendId).getFriendsIds().remove(userId);
    }

    @Override
    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        users.get(userId).getFriendsIds().forEach(id -> {
            friends.add(users.get(id));
        });
        return friends;
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> userFriends = new ArrayList<>();
        List<User> otherFriends = new ArrayList<>();
        users.get(userId).getFriendsIds().forEach(id -> {
            userFriends.add(users.get(id));
        });
        users.get(otherId).getFriendsIds().forEach(id -> {
            otherFriends.add(users.get(id));
        });

        userFriends.retainAll(otherFriends);
        return userFriends;
    }
}
