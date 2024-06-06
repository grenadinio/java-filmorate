package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentMaxId = 1;

    @Override
    public Optional<Film> get(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        film.setId(currentMaxId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(Long id, Long userId) {
        //films.get(id).getUserIdsLiked().add(userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        //films.get(id).getUserIdsLiked().remove(userId);
    }

    @Override
    public List<Film> getTopLikedFilms(Long count) {
        return null;
    }
}
