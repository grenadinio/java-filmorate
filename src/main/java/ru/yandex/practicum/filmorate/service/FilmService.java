package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public Film get(long id) {
        return filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Не найдено фильма с id: " + id));
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }


    public Film create(Film film) {
        validateMpaExistence(film);
        if (film.getGenres() != null) {
            film.setGenres(getUniqueGenres(film));
            validateGenreExistence(film);
        }
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

    private void validateGenreExistence(Film film) {
        List<Long> allGenresIds = genreStorage.getAll().stream().map(Genre::getId).toList();
        for (Genre genre : film.getGenres()) {
            if (!allGenresIds.contains(genre.getId())) {
                throw new IllegalArgumentException("Жанр с id " + genre.getId() + " не существует");
            }
        }
    }

    private void validateMpaExistence(Film film) {
        if (!mpaStorage.getAll().stream().map(MPA::getId).toList().contains(film.getMpa().getId())) {
            throw new IllegalArgumentException("Рейтинг с id " + film.getMpa().getId() + " не существует");
        }
    }

    private void updateFilm(Film film, Film oldFilm) {
        if (film.getName() != null) {
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
        if (film.getMpa() != null) {
            oldFilm.setMpa(film.getMpa());
        }
        if (film.getGenres() != null) {
            oldFilm.setGenres(getUniqueGenres(film));
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

    private List<Genre> getUniqueGenres(Film film) {
        return film.getGenres().stream()
                .collect(Collectors.toMap(Genre::getId, genre -> genre, (oldValue, newValue) -> oldValue))
                .values()
                .stream()
                .toList();
    }

}
