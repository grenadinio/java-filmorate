package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.annotation.DateAfter;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void shouldNotValidateNullName() {
        Film film = new Film();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateBlanklName() {
        Film film = new Film();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateName() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

    @Test
    void shouldNotValidateDescriptionMore200Symbols() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "1");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateDescription() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

    @Test
    void shouldNotValidateReleaseDateBefore28_12_1895() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(DateAfter.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("releaseDate", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateReleaseDate() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

    @Test
    void shouldNotValidateNegativeDuration() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Positive.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("duration", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateZeroDuration() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Positive.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("duration", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateDuration() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }
}
