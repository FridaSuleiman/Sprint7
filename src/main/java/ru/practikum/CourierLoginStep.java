package ru.practikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginStep {


    @Step("Success request return status code 200 and  id")
    public void responseHaveIDCourier(Response response) {
        response.then().statusCode(200).and().assertThat().body("id", notNullValue());
    }

    @Step("Error status code 400 when request has no login or password")
    public void checkAnswerWithoutLoginOrPassword(Response response) {
        response.then().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Error status code 400 if the username or password is incorrect")
    public void checkAnswerWithoutData(Response response) {
        response.then()
                .statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Get id courier for deleting in test")
    public int getIDFOrDeleting(Response courierLoginCredit) {
        int id = courierLoginCredit.then().assertThat()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotEquals(0, id);

        return id;
    }


}