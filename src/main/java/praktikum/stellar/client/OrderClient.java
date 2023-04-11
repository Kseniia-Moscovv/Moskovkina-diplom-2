package praktikum.stellar.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.stellar.client.base.Client;
import praktikum.stellar.model.order.OrderCreate;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    protected static final String ORDER_URI = BASE_URI + "orders";

    @Step("Create order {orderCreate}")
    public ValidatableResponse create(OrderCreate orderCreate, String authorization) {
        return given().spec(getBaseSpec())
                .header("authorization", authorization)
                .body(orderCreate)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Get orders by token {ordersGet}")
    public ValidatableResponse getByUser(String authorization) {
        return  given().spec(getBaseSpec())
                .header("authorization", authorization)
                .when()
                .get(ORDER_URI)
                .then();
    }
}


