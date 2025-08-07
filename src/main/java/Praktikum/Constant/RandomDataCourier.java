package Praktikum.Constant;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataCourier {
    //выбор случайных значений для полей    login, password, firstName (тип строка)
    public static String RANDOM_LOGIN= RandomStringUtils.randomAlphabetic(10);
    public static String RANDOM_PASS = RandomStringUtils.randomNumeric(10);
    public static String RANDOM_FIRSTNAME = RandomStringUtils.randomAlphabetic(10);
    // метод генерации еще одного логина добавлен для тестов курьера так там есть ожидаемый баг и тест падает
    public static void generateNewLogin() {
        RANDOM_LOGIN = RandomStringUtils.randomAlphabetic(10);
    }
}