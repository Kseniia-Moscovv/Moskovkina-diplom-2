package praktikum.stellar.utils;

import org.apache.commons.lang3.RandomStringUtils;
import praktikum.stellar.model.user.UserCreate;

public class UserGenerator {
    public static UserCreate getRandom() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new UserCreate(email, password, name);
    }
}