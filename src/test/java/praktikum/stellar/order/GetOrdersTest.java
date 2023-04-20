package praktikum.stellar.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.stellar.client.IngredientClient;
import praktikum.stellar.client.OrderClient;
import praktikum.stellar.client.UserClient;
import praktikum.stellar.model.ingredient.Ingredient;
import praktikum.stellar.model.ingredient.IngredientsResponse;
import praktikum.stellar.model.order.Order;
import praktikum.stellar.model.order.OrderCreate;
import praktikum.stellar.model.order.OrdersGetResponse;
import praktikum.stellar.model.user.UserCreate;
import praktikum.stellar.model.user.UserLogin;
import praktikum.stellar.utils.UserGenerator;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrdersTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();
    private IngredientClient ingredientClient = new IngredientClient();

    private final int ORDERS_COUNT = 5;

    private UserCreate newUser = UserGenerator.getRandom();
    private String accessToken;

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
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Ingredient element = data.get(i);
            ingredients.add(element.get_id());
        }

        OrderCreate newOrder = new OrderCreate(ingredients);

        for (int i = 0; i < ORDERS_COUNT; i++) {
            orderClient.create(newOrder, accessToken);
        }
    }

    @After
    public void tearDown() {
        if (accessToken != null) userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Positive check to get logged in user orders")
    @Description("Check to get orders by logged in user")
    public void getOrdersByLoggedInUser() {
        OrdersGetResponse ordersGetResponse = orderClient.getByUser(accessToken)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract().as(OrdersGetResponse.class);

        ArrayList<Order> orders = ordersGetResponse.getOrders();

        assertThat("Number of orders does not match", ORDERS_COUNT, Matchers.equalTo(orders.size()));
    }

    @Test
    @DisplayName("Negative check to get logged out user orders")
    @Description("Fail to get orders by logged out user")
    public void getOrdersByLoggedOutUser() {
        orderClient.getByUser("")
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .and()
                .body("message", CoreMatchers.is("You should be authorised"));
    }
}
