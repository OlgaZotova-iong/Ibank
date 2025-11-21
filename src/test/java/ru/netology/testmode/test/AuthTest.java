package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    private SelenideElement loginField = $("[data-test-id='login'] input");
    private SelenideElement passwordField = $("[data-test-id='password'] input");
    private SelenideElement loginButton = $("[data-test-id='action-login']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        loginField.setValue(registeredUser.getLogin());
        passwordField.setValue(registeredUser.getPassword());
        loginButton.click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        loginField.setValue(notRegisteredUser.getLogin());
        passwordField.setValue(notRegisteredUser.getPassword());
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        loginField.setValue(blockedUser.getLogin());
        passwordField.setValue(blockedUser.getPassword());
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        loginField.setValue(wrongLogin);
        passwordField.setValue(registeredUser.getPassword());
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        loginField.setValue(registeredUser.getLogin());
        passwordField.setValue(wrongPassword);
        loginButton.click();
        errorNotification.shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}


