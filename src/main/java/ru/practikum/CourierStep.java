package ru.practikum;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class CourierStep {


    @Step("Courier create success  Status code 201 create and body ok true")
    public void courierAfterCreationSuccess(Response response) {
        response.then().statusCode(201).and().assertThat().body("ok", equalTo(true));
    }

    @Step("Courier status code and body when create without login or password")
    public void courierAfterCreationErr(Response response) {
        response.then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Step("The code and the body of the response when creating a courier when the login is already occupied")
    public void courierCreationLoginAlreadyUsed(Response response) {
        response.then().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

}

