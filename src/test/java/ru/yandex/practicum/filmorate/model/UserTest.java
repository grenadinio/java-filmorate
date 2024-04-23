package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void shouldNotValidateEmailWithoutAt() {
        User user = new User();
        user.setEmail("aaa.aaa1");
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateBlankEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateNullEmail() {
        User user = new User();
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateEmailWithMisplacedAt() {
        User user = new User();
        user.setEmail("@aaa.aaa1");
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

    @Test
    void shouldNotValidateNullLogin() {
        User user = new User();
        user.setEmail("test@test.com");


        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("login", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateBlankLogin() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("");


        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
    }

    @Test
    void shouldNotValidateLoginWithSpaces() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("aa aa");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Pattern.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("login", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateLogin() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

    @Test
    void shouldNotValidateBirthdayFromFuture() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.of(2124, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Нарушений не найдено.");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Past.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("birthday", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateBirthday() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.of(2024, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Найдены нарушения.");
    }

}
