package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public MPA get(Long id) {
        return mpaStorage.get(id).orElseThrow(() -> new NotFoundException("Не найдено рейтинга с id: " + id));
    }

    public List<MPA> getAll() {
        return mpaStorage.getAll();
    }
}
