package praktikum.stellar.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.stellar.client.UserClient;
import praktikum.stellar.model.user.UserCreate;
import praktikum.stellar.model.user.UserEdit;
import praktikum.stellar.utils.UserGenerator;

import java.net.HttpURLConnection;

public class UserEditTest {

    private UserClient userClient = new UserClient();

    private UserCreate newUser = UserGenerator.getRandom();
    private String accessToken;
    private String newLogin = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@ya.ru";
    private String newPassword = "kot2023";
    private String newName = "Kotiks";
    private UserEdit userEdit = new UserEdit(newLogin, newPassword, newName);

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Before
    public void setUp() {
        ValidatableResponse createResponse = userClient.create(newUser);
        String token = createResponse.extract().path("accessToken");
        accessToken = token;
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Positive check to edit user data")
    @Description("Check to edit logged in user")
    public void editLoggedInUserData() {
        userClient.edit(userEdit, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", CoreMatchers.is(true))
                .and()
                .body("user.email", CoreMatchers.is(newLogin))
                .and()
                .body("user.name", CoreMatchers.is(newName));
    }

    @Test
    @DisplayName("Negative check to edit user data")
    @Description("Fail to edit logged out user")
    public void editLoggedOutUserData() {
        userClient.edit(userEdit, "")
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", CoreMatchers.is(false))
                .and()
                .body("message", CoreMatchers.is("You should be authorised"));
    }

}
