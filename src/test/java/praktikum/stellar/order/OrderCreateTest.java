package praktikum.stellar.order;

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
import praktikum.stellar.client.IngredientClient;
import praktikum.stellar.client.OrderClient;
import praktikum.stellar.client.UserClient;
import praktikum.stellar.model.ingredient.Ingredient;
import praktikum.stellar.model.ingredient.IngredientsResponse;
import praktikum.stellar.model.order.OrderCreate;
import praktikum.stellar.model.user.UserCreate;
import praktikum.stellar.model.user.UserLogin;
import praktikum.stellar.utils.UserGenerator;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class OrderCreateTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private IngredientClient ingredientClient = new IngredientClient();

    private UserCreate newUser = UserGenerator.getRandom();
    private String accessToken;
    private ArrayList<String> ingredients = new ArrayList<>();

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new AllureRestAssured());
    }

    @Before
    public void setUp() {
        ValidatableResponse createResponse = userClient.create(newUser);
        UserLogin userLogin = new UserLogin(newUser.getEmail(), newUser.getPassword());
        ValidatableResponse loginResponse = userClient.login(userLogin);
        String token = loginResponse.extract().path("accessToken");
        accessToken = token;

        IngredientsResponse getIngredients = ingredientClient.get();
        ArrayList<Ingredient> data = getIngredients.getData();
        for (int i = 0; i <= 2; i++) {
            Ingredient element = data.get(i);
            ingredients.add(element.get_id());
        }
    }

    @After
    public void tearDown() {
        if (accessToken != null) userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Positive check to create order")
    @Description("Check to create order by logged in user with ingredients")
    public void createOrderByLoggedInUser() {
        OrderCreate newOrder = new OrderCreate(ingredients);

        orderClient.create(newOrder, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("success", CoreMatchers.is(true))
                .and()
                .body("name", CoreMatchers.notNullValue())
                .and()
                .body("order.number", CoreMatchers.notNullValue());
    }

    @Test
    @DisplayName("Negative check to create order")
    @Description("Fail to create order by non logged in user with ingredients")
    public void createOrderByNonLoggedInUser() {
        OrderCreate newOrder = new OrderCreate(ingredients);

        orderClient.create(newOrder, "")
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("success", CoreMatchers.is(false));
    }

    @Test
    @DisplayName("Positive check to create order")
    @Description("Check to create order by logged in user without ingredients")
    public void createOrderWithoutIngredients() {
        OrderCreate newOrder = new OrderCreate(new ArrayList<>());

        orderClient.create(newOrder, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("success", CoreMatchers.is(false))
                .and()
                .body("message", CoreMatchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Positive check to create order")
    @Description("Fail to create order by logged in user with wrong ingredients")
    public void createOrderWithWrongIngredients() {
        ArrayList<String> wrongIngredients = new ArrayList<>();
        wrongIngredients.add("Булочка");
        wrongIngredients.add("Топпинг");
        wrongIngredients.add("Соус");

        OrderCreate newOrder = new OrderCreate(wrongIngredients);

        orderClient.create(newOrder, accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
}
