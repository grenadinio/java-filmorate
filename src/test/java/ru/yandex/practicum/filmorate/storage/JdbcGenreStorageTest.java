package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.JdbcGenreStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcGenreStorageTest {
    private static final long TEST_GENRE_ID = 1L;
    private final JdbcGenreStorage genreStorage;

    @Test
    public void testGetGenreById() {
        Optional<Genre> genreOptional = genreStorage.get(TEST_GENRE_ID);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", TEST_GENRE_ID);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
                        }
                );
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreStorage.getAll();

        assertThat(genres).isNotEmpty();
        assertThat(genres).hasSize(6);
    }
}
