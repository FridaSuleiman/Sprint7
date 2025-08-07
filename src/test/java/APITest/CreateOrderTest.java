package APITest;

import Praktikum.Client.OrderClient;
import Praktikum.CreateOrder;
import Praktikum.OrderStep;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static Praktikum.Constant.EndpointConstant.URL;

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

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color){
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
        return new Object[][]{{"Света", "Тихонова", "Адрес 1", "Лубянка", "71111111111", 1, "2024-09-25", "black", List.of("BLACK")},
                {"Катя", "Иванова", "Адрес 2", "Университет", "72222222222", 2, "2024-09-25", "grey", List.of("GREY")},
                {"Лена", "Петрова", "Адрес 3", "Динамо", "73333333333", 3, "2024-09-25", "black and grey", List.of("BLACK", "GREY")},
                {"Петя", "Четвертый", "Адрес 4", "Фили", "74444444444", 4, "2024-09-25", "не указан", List.of("")},
                {"Вася", "Пятый", "Адрес 5", "Красные ворота", "75555555555", 5, "2023-09-25", "null", null}};

    }


    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }
    @Test
    @DisplayName("Success creating order")
    @Description("Сreating order with different  color.")
    public void creatingOrderWithDifferentColor(){
        OrderStep orderStep=new OrderStep();
        CreateOrder order=new CreateOrder(firstName,  lastName, address,  metroStation, phone, rentTime,  deliveryDate,  comment,  color);
        Response createOrderResponse= OrderClient.createOrder(order);
        orderStep.getOrderTrack(createOrderResponse);
    }
}