package praktikum.stellar.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.stellar.client.UserClient;
import praktikum.stellar.model.user.UserCreate;
import praktikum.stellar.utils.UserGenerator;

import java.net.HttpURLConnection;

public class UserCreateTest {
    private UserClient userClient = new UserClient();
    private String accessToken;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Positive check to create user")
    @Description("Check to create user with valid parameters: email/password/name")
    public void createUserWithValidParameters() {
        UserCreate newUser = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(newUser);

        String token = createResponse.extract().path("accessToken");
        accessToken = token;

        createResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .assertThat()
                .body("success", CoreMatchers.is(true));
    }

    @Test
    @DisplayName ("Negative check to create double user")
    @Description("Fail to check that double user doesn't create")
    public void failToCreateDoubleUser() {
        UserCreate doubleUser = UserGenerator.getRandom();

        ValidatableResponse createResponse = userClient.create(doubleUser);
        String token = createResponse.extract().path("accessToken");
        accessToken = token;

        userClient.create(doubleUser)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("success", CoreMatchers.is(false))
                .and()
                .body("message", CoreMatchers.is("User already exists"));
    }

    @Test
    @DisplayName ("Negative check to create user without required fields")
    @Description("Fail to check that user doesn't create without password")
    public void failToCreateUserWithoutRequiredFields() {
         UserCreate userWithoutPassword = new UserCreate("cutekotik@ya.ru", "", "Kotik");

        userClient.create(userWithoutPassword)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .and()
                .body("success", CoreMatchers.is(false))
                .and()
                .body("message", CoreMatchers.is("Email, password and name are required fields"));
    }
}