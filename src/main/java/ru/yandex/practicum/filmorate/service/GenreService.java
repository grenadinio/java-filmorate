package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre get(Long id) {
        return genreStorage.get(id).orElseThrow(() -> new NotFoundException("Не найдено жанра с id: " + id));
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }
}
