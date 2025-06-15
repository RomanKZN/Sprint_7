import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.OrderApi;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OrderApiTest {

    private final OrderApi orderApi = new OrderApi();

    static class OrderColorScenario {
        String[] colors;
        String description;

        OrderColorScenario(String[] colors, String description) {
            this.colors = colors;
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    static Stream<OrderColorScenario> provideColorScenarios() {
        return Stream.of(
                new OrderColorScenario(new String[]{"BLACK"}, "Цветы: BLACK"),
                new OrderColorScenario(new String[]{"GREY"}, "Цветы: GREY"),
                new OrderColorScenario(new String[]{"BLACK", "GREY"}, "Цвета: BLACK и GREY"),
                new OrderColorScenario(new String[]{}, "Без цвета")
        );
    }

    @ParameterizedTest(name = "Создание заказа - {0}")
    @MethodSource("provideColorScenarios")
    public void testCreateOrderWithVariousColors(OrderColorScenario scenario) {
        Response response = orderApi.createOrder(
                "Ivan",
                "Ivanov",
                "Moscow, Tverskaya, 1",
                1,
                "+79991112233",
                5,
                "2023-12-31",
                "Test comment",
                scenario.colors
        );

        assertEquals(201, response.getStatusCode(), "Статус 201");
        assertTrue(orderApi.isOrderCreated(response), "Заказ считается созданным");
        int trackNumber = orderApi.getTrackNumber(response);
        assertTrue(trackNumber > 0, "Номер трека больше 0");
    }

    @Test
    public void testGetOrders_success() {
        Response response = orderApi.getOrders();
        assertEquals(200, response.getStatusCode());
        assertTrue(orderApi.isGetOrdersSuccess(response));
        assertNotNull(response.jsonPath().getList("orders"));
    }
}