package ru.practikum.client;

import ru.practikum.CreateOrder;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static ru.practikum.constant.EndpointConstant.CANCEL_ORDER_ENDPOINT;
import static ru.practikum.constant.EndpointConstant.CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class OrderClient {

        @Step("Create new order")
    public static Response createOrder(CreateOrder order) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }
    // добавьте метод для отмены заказа
    @Step("Cancel order")
    public static Response cancelOrder(String orderTrack) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                // предполагаю, что API для отмены заказа использует DELETE или POST с телом или параметром
                // пример для DELETE с параметром:
                .queryParam("track", orderTrack)
                .when()
                .delete(CANCEL_ORDER_ENDPOINT); // замените на актуальный URL
    }
}