package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "email")
public class User {
    @NotNull(groups = Update.class)
    Long id;

    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Неверно указан Email.")
    String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелов.")
    String login;

    String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    LocalDate birthday;
}
