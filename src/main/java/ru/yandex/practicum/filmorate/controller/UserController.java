package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public User get(@PathVariable Long userId) {
        log.info("GET /users/{}", userId);
        return userService.get(userId);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        try {
            log.info("POST /users ==> {}", user);
            User newUser = userService.create(user);
            log.info("POST /users <== {}", user);
            return newUser;
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя.", e);
            throw e;
        }
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        try {
            log.info("PUT /users ==> ID: {}, {}", user.getId(), user);
            User updatedUser = userService.update(user);
            log.info("PUT /users <== ID: {}, {}", user.getId(), updatedUser);
            return updatedUser;
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя.", e);
            throw e;
        }
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriend(@PathVariable long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

}
