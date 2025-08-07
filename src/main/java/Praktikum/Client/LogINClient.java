package Praktikum.Client;

import Praktikum.CourierLogin;
import Praktikum.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static Praktikum.Constant.EndpointConstant.LOGIN_COURIER;
import static io.restassured.RestAssured.given;
//  ручка для тестов на логин курьера
public class LogINClient {
    @Step("Create courier login")
    public static Response courierLogin(CourierLogin courierLogin) {
        return given().log().all().header("Content-type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER);
    }
    // ручка для удаления курьера через получение валидных данных
    public static Response courierLoginCredit(Credentials creds){
        return given().log().all().header("Content-type", "application/json")
                .body(creds).when().post(LOGIN_COURIER);
    }
}