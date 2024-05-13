package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film get(long id) {
        return filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Не найдено фильма с id: " + id));
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }


    public Film create(Film film) {
        validateNameUniqueness(film);
        return filmStorage.create(film);
    }


    public Film update(Film film) {
        return filmStorage.get(film.getId())
                .map(original -> {
                    updateFilm(film, original);
                    return filmStorage.update(original);
                })
                .orElseThrow(() -> new NotFoundException("Не найдено фильма с id: " + film.getId()));
    }

    public void addLike(long id, long userId) {
        getFilmOrThrowError(id);
        getUserOrThrowError(userId);
        filmStorage.addLike(id, userId);
    }

    public void removeLike(long id, long userId) {
        getFilmOrThrowError(id);
        getUserOrThrowError(userId);
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getTopLikedFilms(long count) {
        return filmStorage.getTopLikedFilms(count);
    }

    private void validateNameUniqueness(Film film) {
        for (Film existedFilm : filmStorage.getAll()) {
            if (film.getName().equals(existedFilm.getName())) {
                throw new DuplicateDataException("Фильм с таким названием уже добавлен.");
            }
        }
    }

    private void updateFilm(Film film, Film oldFilm) {
        if (film.getName() != null) {
            validateNameUniqueness(film);
            oldFilm.setName(film.getName());
        }
        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }
    }

    private void getUserOrThrowError(long userId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден."));
    }

    private void getFilmOrThrowError(long filmId) {
        filmStorage.get(filmId)
                .orElseThrow(() -> new NotFoundException("Не найдено фильма с id: " + filmId));
    }

}
