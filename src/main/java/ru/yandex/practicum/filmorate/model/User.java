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
    private Long id;

    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Неверно указан Email.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелов.")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;
}
