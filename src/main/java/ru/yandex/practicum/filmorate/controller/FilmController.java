package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Update;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentMaxId = 1;

    @GetMapping
    public Collection<Film> get() {
        log.info("GET /films");
        return films.values();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        try {
            log.info("POST /films ==> {}", film);
            validateNameUniqueness(film);
            film.setId(currentMaxId++);
            films.put(film.getId(), film);
            log.info("POST /films <== {}", film);
            return film;
        } catch (Exception e) {
            log.error("Ошибка при создании фильма.", e);
            throw e;
        }
    }

    @PutMapping
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        try {
            log.info("PUT /films ==> ID = {}, {}", film.getId(), film);
            Film oldFilm = films.get(film.getId());
            updateName(film, oldFilm);
            updateDescription(film, oldFilm);
            updateReleaseDate(film, oldFilm);
            updateDuration(film, oldFilm);
            log.info("PUT /films <== ID = {}, {}", film.getId(), film);
            return oldFilm;
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма.", e);
            throw e;
        }
    }

    private void validateNameUniqueness(Film film) {
        for (Film existedFilm : films.values()) {
            if (film.getName().equals(existedFilm.getName())) {
                throw new DuplicateDataException("Фильм с таким названием уже добавлен.");
            }
        }
    }

    private void updateName(Film film, Film oldFilm) {
        if (film.getName() != null) {
            validateNameUniqueness(film);
            oldFilm.setName(film.getName());
        }
    }

    private void updateDescription(Film film, Film oldFilm) {
        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
        }
    }

    private void updateReleaseDate(Film film, Film oldFilm) {
        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
    }

    private void updateDuration(Film film, Film oldFilm) {
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }
    }

}
