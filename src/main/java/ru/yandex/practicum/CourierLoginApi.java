package ru.yandex.practicum;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CourierLoginApi extends BaseHttpClient {

    private final String apiPath = "/api/v1/courier/login";

    // Авторизация


    public Response login(String login, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);

        return doPostRequest (apiPath, requestBody);
    }

    //Успешная автоизация
    public boolean isLoginSuccessful(Response response) {
        if (response.getStatusCode() == 200) {
            // Проверяем наличие поля id
            return response.jsonPath().getInt("id") != 0;
        }
        return false;
    }

    //Запрос без логина или пароля
    public boolean isMissingCredentialsError(Response response) {
        return response.getStatusCode() == 400 &&
                "Недостаточно данных для входа".equals(response.jsonPath().getString("message"));
    }

    //Запрос с несуществующей парой логин пароль

    public boolean isAccountNotFoundError(Response response) {
        return response.getStatusCode() == 404 &&
                "Учетная запись не найдена".equals(response.jsonPath().getString("message"));
    }

    // Получение ID курьера
    public int getCourierId(Response response) {
        if (isLoginSuccessful(response)) {
            return response.jsonPath().getInt("id");
        }
        return -1;
    }
}
