package ru.yandex.practicum;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class OrderApi extends BaseHttpClient{

    private static final String CREATE_ORDER_PATH = "/api/v1/orders";
    private static final String GET_ORDERS_PATH = "/api/v1/orders";

    //Создание заказа
    public Response createOrder(String firstName, String lastName, String address, int metroStation,
                                String phone, int rentTime, String deliveryDate, String comment, String[] colors) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("address", address);
        requestBody.put("metroStation", metroStation);
        requestBody.put("phone", phone);
        requestBody.put("rentTime", rentTime);
        requestBody.put("deliveryDate", deliveryDate);
        requestBody.put("comment", comment);
        requestBody.put("color", colors);

        return doPostRequest(CREATE_ORDER_PATH, requestBody);
    }

    //Получение списка заказов
    public Response getOrders() {
        return doGetRequest(GET_ORDERS_PATH);
    }

    //Проверка успешного создания заказа
    public boolean isOrderCreated(Response response) {
        return response.getStatusCode() == 201 && response.jsonPath().getInt("track") > 0;
    }

    // Получение трек номера из ответа
    public int getTrackNumber(Response response) {
        if (isOrderCreated(response)) {
            return response.jsonPath().getInt("track");
        }
        return -1;
    }

    // Проверяет успешный ответ при получении заказов
    public boolean isGetOrdersSuccess(Response response) {
        return response.getStatusCode() == 200 && response.jsonPath().getList("orders") != null;
    }
}
