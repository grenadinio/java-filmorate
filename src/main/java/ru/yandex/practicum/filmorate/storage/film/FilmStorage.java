package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> get(Long id);

    List<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    List<Film> getTopLikedFilms(Long count);
}
