package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<MPA> get(Long id);

    List<MPA> getAll();
}
