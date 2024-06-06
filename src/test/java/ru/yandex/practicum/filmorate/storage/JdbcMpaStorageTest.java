package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.JdbcMpaStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcMpaStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcMpaStorageTest {
    private static final long TEST_MPA_ID = 1L;
    private final JdbcMpaStorage mpaStorage;

    @Test
    public void testGetMpaById() {
        Optional<MPA> mpaOptional = mpaStorage.get(TEST_MPA_ID);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", TEST_MPA_ID);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }

    @Test
    public void testGetAllMpas() {
        List<MPA> mpas = mpaStorage.getAll();

        assertThat(mpas).isNotEmpty();
        assertThat(mpas).hasSize(5);
    }
}
