package Praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static Praktikum.Constant.EndpointConstant.ORDERS_LIST;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class OrderStep {


    @Step("After create order body have status code 201 and track ")
    public void getOrderTrack(Response response) {
        response.then().statusCode(201).and().assertThat().body("track", notNullValue());

    }

    @Step("Get order list")
    public Response getOrderList() {
        return given().log().all().get(ORDERS_LIST);
    }

    @Step("List of orders is not empty and status code 200")
    public void orderListNotNull(Response response) {
        response.then()
                .statusCode(200).and().assertThat().body("orders", notNullValue());
    }
    @Step(" check that there are orders in the response and this is a list")
    public void orderListIsList(Response response) {
        response.then().body("orders", instanceOf(List.class));
    }
}