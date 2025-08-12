package ru.apitests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import ru.practikum.client.OrderClient;
import ru.practikum.CreateOrder;
import ru.practikum.OrderStep;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Integer rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    // Поле для хранения номера заказа
    private String orderTrack;

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone,
                           int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"Света", "Тихонова", "Адрес 1", "Лубянка", "71111111111", 1, "2024-09-25", "black", List.of("BLACK")},
                {"Катя", "Иванова", "Адрес 2", "Университет", "72222222222", 2, "2024-09-25", "grey", List.of("GREY")},
                {"Лена", "Петрова", "Адрес 3", "Динамо", "73333333333", 3, "2024-09-25", "black and grey", List.of("BLACK", "GREY")},
                {"Петя", "Четвертый", "Адрес 4", "Фили", "74444444444", 4, "2024-09-25", "не указан", List.of("")},
                {"Вася", "Пятый", "Адрес 5", "Красные ворота", "75555555555", 5, "2023-09-25", null, null}
        };
    }

    @Before
    public void setUp() {
        // Можно подготовить что-то перед каждым тестом
        // Пока ничего не нужно
    }

    @Test
    @DisplayName("Success creating order")
    @Description("Creating order with different color.")
    public void creatingOrderWithDifferentColor() {
        OrderStep orderStep = new OrderStep();
        CreateOrder order = new CreateOrder(firstName, lastName, address, metroStation, phone,
                rentTime, deliveryDate, comment, color);
        Response createOrderResponse = OrderClient.createOrder(order);
        createOrderResponse.then().statusCode(201);
        // Получаем номер заказа из ответа
        orderStep.getOrderTrack(createOrderResponse);
    }

    @After
    public void tearDown() {
        if (orderTrack != null) {
            Response cancelResponse = OrderClient.cancelOrder(orderTrack);
            cancelResponse.then().statusCode(200);
        }
    }
}