package ru.praktikum.client;

import ru.praktikum.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static ru.praktikum.constant.EndpointConstant.CREATE_COURIER;
import static ru.praktikum.constant.EndpointConstant.DELETE_COURIER;
import static io.restassured.RestAssured.given;

public class CourierClient {
    @Step("Create courier")
    public static Response createCourier(Courier courier) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }
    @Step("Deleting courier after test ")
    public  static Response deleteCourier(int id){
        return given().log().all().header("Content-type", "application/json")
                .body(Map.of("id", id))
                .when()
                .delete(DELETE_COURIER+id);
    }
}