package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.JdbcFilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(JdbcFilmStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFilmStorageTest {
    private static final long TEST_FILM_ID = 1L;
    private static final String TEST_FILM_NAME = "TestFilm1";
    private final JdbcFilmStorage filmStorage;

    @Test
    public void getFilmShouldReturnFilmWithTestFilmId() {
        Optional<Film> filmOptional = filmStorage.get(TEST_FILM_ID);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", TEST_FILM_ID);
                            assertThat(film).hasFieldOrPropertyWithValue("name", TEST_FILM_NAME);
                        }
                );
    }

    @Test
    public void getAllFilmsShouldReturnTwoFilms() {
        List<Film> films = filmStorage.getAll();
        assertThat(films).isNotEmpty();
        assertThat(films).hasSize(2);
    }

    @Test
    public void createFilmShouldInsertNewFilmToDatabase() {
        Film newFilm = new Film();
        MPA mpa = new MPA();
        mpa.setId(1L);

        newFilm.setName("New Name");
        newFilm.setDescription("New Description");
        newFilm.setReleaseDate(LocalDate.now());
        newFilm.setDuration(10);
        newFilm.setMpa(mpa);

        Film createdFilm = filmStorage.create(newFilm);
        assertThat(createdFilm).isNotNull();
        assertThat(createdFilm.getId()).isNotNull();

    }

    @Test
    public void updateFilmShouldUpdateExistingFilmInDatabase() {
        Optional<Film> filmOptional = filmStorage.get(TEST_FILM_ID);
        Film film = filmOptional.orElseThrow();
        film.setName("Updated Name");

        Film updatedFilm = filmStorage.update(film);
        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void addAndRemoveLikeShouldInsertAndDeleteLikesInDatabase() {
        Long userId = 1L;
        Long filmId = 1L;

        assertFalse(filmStorage.hasUserLikedFilm(userId, filmId));

        filmStorage.addLike(filmId, userId);
        assertTrue(filmStorage.hasUserLikedFilm(userId, filmId));

        filmStorage.removeLike(filmId, userId);
        assertFalse(filmStorage.hasUserLikedFilm(userId, filmId));
    }

    @Test
    public void getTopLikedFilmsShouldReturnTopFilmsFromDatabase() {
        filmStorage.addLike(1L, 1L);
        filmStorage.addLike(1L, 2L);
        filmStorage.addLike(2L, 1L);

        List<Film> topLikedFilms = filmStorage.getTopLikedFilms(2L);

        assertEquals(2L, topLikedFilms.size());

        assertEquals(1L, topLikedFilms.getFirst().getId());
    }
}
