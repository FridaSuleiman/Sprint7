package ru.apitests;

import ru.practikum.client.CourierClient;
import ru.practikum.client.LogInClient;
import ru.practikum.Courier;
import ru.practikum.CourierLogin;
import ru.practikum.CourierLoginStep;
import ru.practikum.Credentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static ru.practikum.constant.EndpointConstant.URL;
import static ru.practikum.constant.RandomDataCourier.*;

public class LoginCourierTest {
    CourierLoginStep courierLoginStep = new CourierLoginStep();
    Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        // Создаем нового курьера с рандомными данными
        courier = new Courier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_FIRSTNAME);
        // Создаем курьера через API
        CourierClient.createCourier(courier);
    }

    @DisplayName("Courier login is success")
    @Description("Success request return status code 200 and id")
    @Test
    public void courierLoginSuccess() {
        CourierLogin courierLogin = new CourierLogin(RANDOM_LOGIN, RANDOM_PASS);
        Response loginResponse = LogInClient.courierLogin(courierLogin);
        courierLoginStep.responseHaveIDCourier(loginResponse);
    }

    @DisplayName("Courier login is wrong")
    @Description("Request returns status code 404 and message")
    @Test
    public void courierLoginWrong() {
        CourierLogin courierLogin = new CourierLogin("wrong", RANDOM_PASS);
        Response wrongLogin = LogInClient.courierLogin(courierLogin);
        courierLoginStep.checkAnswerWithoutLoginOrPassword(wrongLogin);
    }

    @DisplayName("Courier password is wrong")
    @Description("Request returns status code 404 and message")
    @Test
    public void courierPasswordWrong() {
        CourierLogin courierLogin = new CourierLogin(RANDOM_LOGIN, "wrong");
        Response wrongPassword = LogInClient.courierLogin(courierLogin);
        courierLoginStep.checkAnswerWithoutLoginOrPassword(wrongPassword);
    }

    @DisplayName("login courier without login")
    @Description("login without login and request returns status code 400")
    @Test
    public void courierLoginWithoutLogin() {
        CourierLogin courierLogin = new CourierLogin("", RANDOM_PASS);
        Response response = LogInClient.courierLogin(courierLogin);
        courierLoginStep.checkAnswerWithoutData(response);
    }

    @DisplayName("login courier without password")
    @Description("login without password and request returns status code 400")
    @Test
    public void courierLoginWithoutPassword() {
        CourierLogin courierLogin = new CourierLogin(RANDOM_LOGIN, "");
        Response response = LogInClient.courierLogin(courierLogin);
        courierLoginStep.checkAnswerWithoutData(response);
    }

    @DisplayName("login courier without data")
    @Description("login without data and request returns status code 400")
    @Test
    public void courierLoginWithoutData() {
        CourierLogin courier= new CourierLogin("", "");
        Response response = LogInClient.courierLogin(courier);
        courierLoginStep.checkAnswerWithoutData(response);
    }

    @After
    public void deleteCourier() {
        // Получаем ID созданного курьера для удаления
        Credentials creds= Credentials.fromCourier(courier);
        Response loginResponse = LogInClient.courierLoginCredit(creds);
        courierLoginStep.responseHaveIDCourier(loginResponse);

        int courierID = courierLoginStep.getIDFOrDeleting(loginResponse);

        if (courierID != 0) {
            CourierClient.deleteCourier(courierID);
        }
    }
}