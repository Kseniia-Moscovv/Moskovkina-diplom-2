package praktikum.stellar.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.stellar.client.UserClient;
import praktikum.stellar.model.user.UserCreate;
import praktikum.stellar.model.user.UserLogin;
import praktikum.stellar.utils.UserGenerator;

import java.net.HttpURLConnection;

public class UserLoginTest {
    private UserClient userClient = new UserClient();

    private UserCreate newUser = UserGenerator.getRandom();
    private String accessToken;

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
    @DisplayName("Positive check to login user")
    @Description("Check to login user with valid parameters: email/password/name")
    public void loginWithValidParameters() {
        UserLogin userLogin = new UserLogin(newUser.getEmail(), newUser.getPassword());
        userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", CoreMatchers.is(true))
                .and()
                .body("accessToken", CoreMatchers.notNullValue())
                .and()
                .body("refreshToken", CoreMatchers.notNullValue())
                .and()
                .body("user.email", CoreMatchers.is(newUser.getEmail()))
                .and()
                .body("user.name", CoreMatchers.is(newUser.getName()));
    }

    @Test
    @DisplayName ("Negative check to login user with wrong password")
    @Description("Fail to check that user can't login with wrong login OR password")
    public void failToLoginUserWithWrongPassword() {
        UserLogin userLogin = new UserLogin(newUser.getEmail(), "murmurmur");
        userClient.login(userLogin)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", CoreMatchers.is(false))
                .and()
                .body("message", CoreMatchers.is( "email or password are incorrect"));
    }
}
