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
import org.junit.rules.TestWatcher;

import static ru.practikum.constant.EndpointConstant.URL;

// Исправленный импорт Description из Allure
// import io.qameta.allure.Description; // оставляем как есть, он нужен для аннотации
// Удаляем неправильный импорт с псевдонимом

public class CreateCourierTest {
    // Используем Integer для ID, чтобы отличать "не создан" (null) от 0
    private Integer courierID = null;

    // Флаг для отслеживания падения теста
    private boolean testFailed = false;

    // Правило для отслеживания ошибок теста
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, org.junit.runner.Description description) {
            testFailed = true; // отмечаем, что тест упал
        }
    };

    // Объекты для работы с шагами и клиентами
    private final CourierStep courierStep = new CourierStep();
    private final CourierLoginStep courierLoginStep = new CourierLoginStep();

    @Before
    public void setUp() {
        RestAssured.baseURI = URL; // базовый URL API
        RandomDataCourier.generateNewLogin(); // генерируем новый логин перед каждым тестом
        testFailed = false; // сбрасываем флаг перед каждым тестом
        courierID = null; // сбрасываем ID курьера перед каждым тестом
    }

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

        // Проверяем, что сервер возвращает ошибку 400 или другую ожидаемую ошибку
        courierStep.courierAfterCreationErr(createResponse);

        // Нет ID для удаления, так как создание не удалось успешно
    }

    @Test
    @DisplayName("Creating courier without password")
    @Description("Creating courier without password and checking the response")
    public void creatingCourierWithoutPasswordBadRequest() {
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, "", RandomDataCourier.RANDOM_FIRSTNAME);
        Response createResponse = CourierClient.createCourier(courier);

        // Проверяем ошибку при создании без пароля
        courierStep.courierAfterCreationErr(createResponse);

        // Нет ID для удаления, так как создание не удалось успешно
    }

    @Test
    @DisplayName("Creating courier without firstName")
    @Description("Creating courier without firstName and checking the response")
    public void creatingCourierWithoutFirstName() {
        // Создаем объект Courier без firstName (null)
        Courier courier = new Courier(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS, null);

        Response createResponse = CourierClient.createCourier(courier);

        // Проверяем, что создание прошло успешно (статус 201)
        createResponse.then().statusCode(201);

        // Логинимся и сохраняем ID для удаления
        Credentials creds = new Credentials(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS);
        Response loginResponse = LogInClient.courierLoginCredit(creds);
        this.courierID = courierLoginStep.getIDFOrDeleting(loginResponse);
    }

    @After
    public void deleteCourier() {
        if (courierID != null) {
            try {
                CourierClient.deleteCourier(courierID);
            } catch (Exception e) {
                // Можно залогировать ошибку или оставить пустым,
                // чтобы не мешать выполнению других тестов.
            }
            courierID = null;
        }

        // В случае падения теста — выполнить вход курьера для получения ID (если он не был получен)
        if (testFailed && this.courierID == null) {
            try {
                Credentials creds = new Credentials(RandomDataCourier.RANDOM_LOGIN, RandomDataCourier.RANDOM_PASS);
                Response loginResponse = LogInClient.courierLoginCredit(creds);
                this.courierID = courierLoginStep.getIDFOrDeleting(loginResponse);
            } catch (Exception e) {
                // Логировать или игнорировать ошибки входа при падении теста.
            }
        }
    }
}