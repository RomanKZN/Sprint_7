import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.CourierCreateApi;
import ru.yandex.practicum.CourierDeleteApi;
import ru.yandex.practicum.CourierLoginApi;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CourierCreateTest {

    // Общий экземпляр API
    private CourierCreateApi courierApi;

    String login;
    String password = "pass123";
    String firstName = "Auto";

    @BeforeEach
    public void setUp() {
        login = "autoTest_" + System.currentTimeMillis();
        courierApi = new CourierCreateApi(login, password, firstName);
    }

    @Test
    public void testCreateCourier() {
        Response response = createCourier();
        verifyCreateSuccess(response);
    }

    @Test
    public void testRequestWithoutLogin() {
        courierApi = new CourierCreateApi("" ,password,firstName);
        Response response = createCourier();
        verifyMissingDataError(response);
    }

    @Test
    public void testRequestWithoutPassword() {
        courierApi = new CourierCreateApi(login, "", firstName);
        Response response = createCourier();
        verifyMissingDataError(response);
    }

    @Test
    public void testingQueryWithDuplicateLogin() {
        // Создаем первого курьера
        Response response1 = createCourier();
        verifyCreateSuccess(response1);

        // Второй — тот же логин
        Response response2 = createCourier();
        verifyLoginAlreadyExistsError(response2);
    }

    @AfterEach
    public void tearDown() {
        Response loginResponse = new CourierLoginApi().login(login, password);
        int courierId = new CourierLoginApi().getCourierId(loginResponse);
        if(courierId != -1) {
            new CourierDeleteApi().deleteCourier(courierId);
        }
    }


    @Step("Создать курьера с логином: {login}")
    public Response createCourier() {
        return courierApi.createCourier();
    }

    @Step("Проверка успешного создания курьера")
    public void verifyCreateSuccess(Response response) {
        assertTrue( courierApi.isCreateSuccess(response),"Ожидалось успешное создание курьера");
    }

    @Step("Проверка ошибки отсутствия логина или пароля")
    public void verifyMissingDataError(Response response) {
        assertTrue(courierApi.isMissingDataError(response), "Ожидалась ошибка без логина или пароля");
    }

    @Step("Проверка ошибки с повторяющимся логином")
    public void verifyLoginAlreadyExistsError(Response response) {
        assertTrue(courierApi.isLoginAlreadyExistsError(response),"Ожидалась ошибка с повторяющимся логином");
    }
}