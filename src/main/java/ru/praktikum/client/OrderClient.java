package ru.praktikum.client;

import ru.praktikum.CreateOrder;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static ru.praktikum.constant.EndpointConstant.CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Create new order")
    public  static Response createOrder(CreateOrder order) {
        return given().log().all().header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER);

    }
}