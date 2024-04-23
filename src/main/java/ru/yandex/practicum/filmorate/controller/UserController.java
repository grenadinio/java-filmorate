package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Update;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long currentMaxId = 1;

    @GetMapping
    public Collection<User> get() {
        log.info("GET /users");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            log.info("POST /users ==> {}", user);
            validateEmailUniqueness(user);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(currentMaxId++);
            users.put(user.getId(), user);
            log.info("POST /users <== {}", user);
            return user;
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя.", e);
            throw e;
        }
    }

    @PutMapping
    public User update(@Validated(Update.class) @RequestBody User user) {
        try {
            log.info("PUT /users ==> ID: {}, {}", user.getId(), user);
            User oldUser = users.get(user.getId());
            updateEmail(user, oldUser);
            updateLogin(user, oldUser);
            updateName(user, oldUser);
            updateBirthday(user, oldUser);
            log.info("PUT /users <== ID: {}, {}", user.getId(), user);
            return oldUser;
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя.", e);
            throw e;
        }
    }

    private void validateEmailUniqueness(User user) {
        for (User existedUser : users.values()) {
            if (user.getEmail().equals(existedUser.getEmail())) {
                throw new DuplicateDataException("Этот email уже используется.");
            }
        }
    }

    private void updateEmail(User user, User oldUser) {
        if (user.getEmail() != null) {
            validateEmailUniqueness(user);
            oldUser.setEmail(user.getEmail());
        }
    }

    private void updateLogin(User user, User oldUser) {
        if (user.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }
    }

    private void updateName(User user, User oldUser) {
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
    }

    private void updateBirthday(User user, User oldUser) {
        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }
    }
}
