package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Update;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{filmId}")
    public Film get(@PathVariable Long filmId) {
        log.info("GET /films/{}", filmId);
        return filmService.get(filmId);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("GET /films");
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        try {
            log.info("POST /films ==> {}", film);
            Film newFilm = filmService.create(film);
            log.info("POST /films <== {}", film);
            return newFilm;
        } catch (Exception e) {
            log.error("Ошибка при создании фильма.", e);
            throw e;
        }
    }

    @PutMapping
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        try {
            log.info("PUT /films ==> ID = {}, {}", film.getId(), film);
            Film updatedFilm = filmService.update(film);
            log.info("PUT /films <== ID = {}, {}", film.getId(), updatedFilm);
            return updatedFilm;
        } catch (Exception e) {
            log.error("Ошибка при обновлении фильма.", e);
            throw e;
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopLikedFilms(@RequestParam(value = "count", defaultValue = "10") long count) {
        return filmService.getTopLikedFilms(count);
    }

}
