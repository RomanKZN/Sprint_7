package ru.yandex.practicum;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CourierCreateApi extends BaseHttpClient {

    private final String apiPatch = "/api/v1/courier";
    private String login;
    private String password;
    private String firstName;
    //private Response response;

    // Конструктор для установки данных
    public CourierCreateApi(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // Геттеры
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }




    //Создание нового курьера
    public Response createCourier() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("login", getLogin());
        requestBody.put("password", getPassword());
        requestBody.put("firstName", getFirstName());

        return doPostRequest(apiPatch, requestBody);
    }

    // Успешное создание
    public boolean isCreateSuccess(Response response) {
        if (response.getStatusCode() == 201) {
            Boolean ok = response.jsonPath().getBoolean("ok");
            return Boolean.TRUE.equals(ok);
        }
        return false;
    }

    // Ошибка запроса без логина иил пароля
    public boolean isMissingDataError(Response response) {
        return response.getStatusCode() == 400 &&
                response.jsonPath().getString("message").equals("Недостаточно данных для создания учетной записи");
    }

    // Запрос с повторяющим логином
    public boolean isLoginAlreadyExistsError(Response response) {
        return response.getStatusCode() == 409 &&
                response.jsonPath().getString("message").equals("Этот логин уже используется. Попробуйте другой.");
    }

    //Общий метод для обработки ответа при создании курьера
    public String getCreateResponseMessage(Response response) {
        return response.getBody().asString();

    }
}
