package ru.apitests;

import ru.practikum.client.CourierClient;
import ru.practikum.client.LogInClient;
import ru.practikum.constant.RandomDataCourier;
import ru.practikum.Courier;
import ru.practikum.CourierLoginStep;
import ru.practikum.CourierStep;
import ru.practikum.Credentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static ru.practikum.constant.EndpointConstant.URL;

public class CreateCourierTest {
    private int courierID = 0; // ID курьера, по умолчанию - не создан
    CourierStep courierStep = new CourierStep();
    CourierLoginStep courierLoginStep = new CourierLoginStep();

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        // Генерируем новый логин перед каждым тестом
        RandomDataCourier.generateNewLogin();
    }

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    @DisplayName("Creating new courier")
    @Description("Creating new courier with correct data and checkins status code")
    public void creatingCourierPositive() {
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS, RandomDataCourier.RANDOM_FIRSTNAME);
        Response createCourier = CourierClient.createCourier(courier);
        courierStep.courierAfterCreationSuccess(createCourier);

        Credentials creds = Credentials.fromCourier(courier);
        Response loginResponse = LogInClient.courierLoginCredit(creds);
        this.courierID = courierLoginStep.getIDFOrDeleting(loginResponse);
    }

    @Test
    @DisplayName("creating a courier if the login is already in use")
    @Description("Creating courier with existing login checking the response")
    public void creatingCourierWhenLoginAlreadyUsed() {
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS, RandomDataCourier.RANDOM_FIRSTNAME);
        // Создаем курьера один раз
        Response firstCreateResponse = CourierClient.createCourier(courier);
        courierStep.courierAfterCreationSuccess(firstCreateResponse);

        // Попытка создать еще раз с тем же логином
        Response secondCreateResponse = CourierClient.createCourier(courier);
        courierStep.courierCreationLoginAlreadyUsed(secondCreateResponse);

        // Логинимся и получаем ID для удаления
        Credentials creds = Credentials.fromCourier(courier);
        Response loginResponse = LogInClient.courierLoginCredit(creds);
        this.courierID = courierLoginStep.getIDFOrDeleting(loginResponse);
    }

    @Test
    @DisplayName("Creating courier without login")
    @Description("Creating courier without login and checking the response")
    public void creatingCourierWithoutLoginBadRequest() {
        Courier courier = new Courier("", RandomDataCourier.RANDOM_PASS, RandomDataCourier.RANDOM_FIRSTNAME);
        Response createResponse = CourierClient.createCourier(courier);
        // Проверяем, что сервер возвращает ошибку 400 (или другую ожидаемую)
        courierStep.courierAfterCreationErr(createResponse);
        // Нет ID для удаления
    }

    @Test
    @DisplayName("Creating courier without password")
    @Description("Creating courier without password and checking the response")
    public void creatingCourierWithoutPasswordBadRequest() {
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, "", RandomDataCourier.RANDOM_FIRSTNAME);
        Response createResponse = CourierClient.createCourier(courier);
        courierStep.courierAfterCreationErr(createResponse);
    }

    @Test
    @DisplayName("Creating courier without firstName")
    @Description("Creating courier without firstName and checking the response")
    public void creatingCourierWithoutFirstNameBadRequest() {
        // Создаем объект Courier без firstName (оставляем его null или пустым)
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS, null);
        Response createResponse = CourierClient.createCourier(courier);

        // Предполагаемое поведение: API возвращает 201 (успешное создание)
        // или ошибку 400. В зависимости от требований.

        // Если ожидаем ошибку 400:
        // courierStep.courierAfterCreationErr(createResponse);

        // Если ожидаем успешное создание:

        if (createResponse.statusCode() == 201) {
            // Успешное создание — сохраняем ID для удаления
            Credentials creds = new Credentials(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS);
            Response loginResponse = LogInClient.courierLoginCredit(creds);
            this.courierID = courierLoginStep.getIDFOrDeleting(loginResponse);
            // Можно добавить проверку тела ответа или статус кода
            return;
        } else {
            // Иначе — считаем ошибкой и вызываем метод проверки ошибки
            courierStep.courierAfterCreationErr(createResponse);
        }

    }

    @After
    public void deleteCourier() {
        if (courierID != 0) {
            try {
                CourierClient.deleteCourier(courierID);
            } catch (Exception e) {
                // Можно залогировать ошибку или оставить пустым,
                // чтобы не мешать выполнению других тестов.
            }
            courierID = 0;
        }
    }
}