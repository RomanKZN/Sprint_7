import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.CourierCreateApi;
import ru.yandex.practicum.CourierDeleteApi;
import ru.yandex.practicum.CourierLoginApi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierLoginTest {

    private CourierCreateApi courierApi;
    private CourierLoginApi courierLoginApi;

    String login;
    String password = "pass123";
    String firstName = "Auto";

    @BeforeEach
    public void setUp() {
        login = "autoTest_" + System.currentTimeMillis();
        courierApi = new CourierCreateApi(login, password, firstName);
        courierApi.createCourier(); // создаем курьера перед тестами
        courierLoginApi = new CourierLoginApi();
    }

    @Test
    public void testLoginSuccess() {
        Response response = loginAndVerify(login, password);
        verifyLoginSuccessful(response);
        int courierId = getCourierId(response);
        assertTrue(courierId > 0, "ID курьера должен быть больше 0");
    }

    @Test
    public void testLoginWithWrongPassword() {
        Response response = loginAndVerify(login, "wrongPass");
        verifyLoginFailure(response);
        verifyAccountNotFoundError(response);
    }

    @Test
    public void testLoginWithNonExistingUser() {
        Response response = loginAndVerify("nonexistentUser", password);
        verifyLoginFailure(response);
        verifyAccountNotFoundError(response);
    }

    @Test
    public void testLoginMissingLogin() {
        Response response = loginAndVerify("", password);
        verifyLoginFailure(response);
        verifyMissingCredentialsError(response);
    }

    @Test
    public void testLoginMissingPassword() {
        Response response = loginAndVerify(login, "");
        verifyLoginFailure(response);
        verifyMissingCredentialsError(response);
    }

    @AfterEach
    public void tearDown() {
        Response loginResponse = new CourierLoginApi().login(login, password);
        int courierId = getCourierId(loginResponse);
        if (courierId != -1) {
            new CourierDeleteApi().deleteCourier(courierId);
        }
    }

    @Step("Логин и получение ответа для логина: {login}")
    private Response loginAndVerify(String login, String password) {
        return courierLoginApi.login(login, password);
    }

    @Step("Проверка успешного входа")
    private void verifyLoginSuccessful(Response response) {
        assertTrue(courierLoginApi.isLoginSuccessful(response), "Ожидался успешный вход");
    }

    @Step("Проверка неуспешной попытки входа")
    private void verifyLoginFailure(Response response) {
        assertFalse(courierLoginApi.isLoginSuccessful(response), "Вход должен был быть неуспешным");
    }

    @Step("Проверка ошибки 'Учетная запись не найдена'")
    private void verifyAccountNotFoundError(Response response) {
        assertTrue(courierLoginApi.isAccountNotFoundError(response), "Должна быть ошибка 'Учетная запись не найдена'");
    }

    @Step("Проверка ошибки 'Недостаточно данных для входа'")
    private void verifyMissingCredentialsError(Response response) {
        assertTrue(courierLoginApi.isMissingCredentialsError(response), "Должна быть ошибка 'Недостаточно данных для входа'");
    }

    @Step("Получение ID курьера из ответа")
    private int getCourierId(Response response) {
        return courierLoginApi.getCourierId(response);
    }
}