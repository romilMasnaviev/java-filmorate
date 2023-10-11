package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    //test mail parameter
    private static final String emptyMail = "{\"id\":1,\"email\":\"\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    private static final String mailWithoutAt = "{\"id\":1,\"email\":\"email.ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    private static final String validMail = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    //test login parameter
    private static final String emptyLogin = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    private static final String loginWithWhiteSpace = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"log in\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    private static final String validLogin = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    //test name parameter
    private static final String emptyName = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"\",\"birthday\":\"0001-01-01\"}";
    private static final String validName = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    //test birthday parameter
    private static final String earlierNowBirthday = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"0001-01-01\"}";
    private static final String laterNowBirthday = "{\"id\":1,\"email\":\"email@ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"3000-01-01\"}";
    private static UserController userController;

    @BeforeEach
    public void setup() {
        userController = new UserController();
    }

    @Test
    public void addUser_emptyMail_getValidationException() {
        assertThrows(ValidationException.class, () -> userController.addUser(emptyMail));
    }

    @Test
    public void addUser_mailWithoutAt_getValidationException() {
        assertThrows(ValidationException.class, () -> userController.addUser(mailWithoutAt));
    }

    @Test
    public void addUser_validMail_getAddedUser() {
        User expecetedUser = gson.fromJson(validMail, User.class);
        User user = userController.addUser(validMail);
        assertEquals(expecetedUser, user);
    }

    @Test
    public void addUser_emptyLogin_getValidationException() {
        assertThrows(ValidationException.class, () -> userController.addUser(emptyLogin));
    }

    @Test
    public void addUser_loginWithWhiteSpace_getValidationException() {
        assertThrows(ValidationException.class, () -> userController.addUser(loginWithWhiteSpace));
    }

    @Test
    public void addUser_validLogin_getAddedUser() {
        User expecetedUser = gson.fromJson(validLogin, User.class);
        User user = userController.addUser(validLogin);
        assertEquals(expecetedUser, user);
    }

    @Test
    public void addUser_emptyName_getAddedUserWithLoginAsName() {
        User expecetedUser = gson.fromJson(emptyName, User.class).toBuilder().name("login").build();
        User user = userController.addUser(emptyName);
        assertEquals(expecetedUser, user);
    }

    @Test
    public void addUser_validName_getAddedUser() {
        User expecetedUser = gson.fromJson(validName, User.class);
        User user = userController.addUser(validName);
        assertEquals(expecetedUser, user);
    }

    @Test
    public void addUser_earlierNowBirthday_getAddedUser() {
        User expecetedUser = gson.fromJson(earlierNowBirthday, User.class);
        User user = userController.addUser(earlierNowBirthday);
        assertEquals(expecetedUser, user);
    }

    @Test
    public void addUser_laterNowBirthday_getValidationException() {
        assertThrows(ValidationException.class, () -> userController.addUser(laterNowBirthday));
    }

}