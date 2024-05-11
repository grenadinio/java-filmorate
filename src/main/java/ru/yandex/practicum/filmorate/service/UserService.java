package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User get(long userId) {
        return userStorage.get(userId).orElseThrow(() -> new NotFoundException("User not found with " + userId));
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        validateEmailUniqueness(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.get(user.getId())
                .map(original -> {
                    updateUser(user, original);
                    return userStorage.update(original);
                })
                .orElseThrow(() -> new NotFoundException("User not found with " + user.getId()));
    }

    public void addFriend(long userId, long friendId) {
        getUserOrThrowError(userId);
        getUserOrThrowError(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        getUserOrThrowError(userId);
        getUserOrThrowError(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(long userId) {
        getUserOrThrowError(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        getUserOrThrowError(userId);
        getUserOrThrowError(otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }

    private void validateEmailUniqueness(User user) {
        for (User existedUser : userStorage.getAll()) {
            if (user.getEmail().equals(existedUser.getEmail())) {
                throw new DuplicateDataException("Этот email уже используется.");
            }
        }
    }

    private void updateUser(User user, User originalUser) {
        if (Objects.nonNull(user.getEmail())) {
            validateEmailUniqueness(user);
            originalUser.setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getLogin())) {
            originalUser.setLogin(user.getLogin());
        }
        if (Objects.nonNull(user.getName()) && !user.getName().isBlank()) {
            originalUser.setName(user.getName());
        }
        if (Objects.nonNull(user.getBirthday())) {
            originalUser.setBirthday(user.getBirthday());
        }
    }

    private void getUserOrThrowError(long userId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("User not found with " + userId));
    }
}
