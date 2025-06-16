package ru.yandex.practicum;

import io.restassured.response.Response;

public class CourierDeleteApi extends BaseHttpClient {

    // Метод удаления курьера
    public Response deleteCourier(int id) {
        String deletePath = "/api/v1/courier/" + id;

        return doDeleteRequest(deletePath);
    }

    // Проверка успешного удаления
    public boolean isDeletionSuccessful(Response response) {
        return response.getStatusCode() == 200 && response.jsonPath().getBoolean("ok");
    }

    // Запрос без Id
    public boolean isMissingIdError(Response response) {
        return response.getStatusCode() == 400 &&
                "Недостаточно данных для удаления курьера".equals(response.jsonPath().getString("message"));
    }

    //Запрос с несуществующим id курьера
    public boolean isCourierNotFoundError(Response response) {
        return response.getStatusCode() == 404 &&
                "Курьера с таким id нет".equals(response.jsonPath().getString("message"));
    }
}
