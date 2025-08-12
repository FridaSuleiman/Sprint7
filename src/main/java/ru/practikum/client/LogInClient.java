package ru.practikum.client;

import ru.practikum.CourierLogin;
import ru.practikum.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static ru.practikum.constant.EndpointConstant.LOGIN_COURIER;
import static io.restassured.RestAssured.given;

public class LogInClient {

    /**
     * Выполняет логин курьера по данным.
     *
     * @param courierLogin объект с логином и паролем
     * @return Response от API
     */
    @Step("Создать логин курьера: {courierLogin}")
    public static Response courierLogin(CourierLogin courierLogin) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER);
    }

    /**
     * Выполняет логин курьера с использованием учетных данных.
     *
     * @param creds объект с логином и паролем
     * @return Response от API
     */
    @Step("Создать логин курьера с учетными данными: {creds}")
    public static Response courierLoginCredit(Credentials creds) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(creds)
                .when()
                .post(LOGIN_COURIER);
    }
}