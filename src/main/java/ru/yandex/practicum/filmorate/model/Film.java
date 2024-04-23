package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.DateAfter;
import ru.yandex.practicum.filmorate.validator.Update;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "name")
public class Film {
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Описание не может быть больше 200 символов.")
    private String description;

    @DateAfter(value = "28.12.1895")
    private LocalDate releaseDate;

    @Positive(message = "Длительность фильма должна быть больше 0.")
    private Integer duration;
}
