package praktikum.stellar.client;

import io.qameta.allure.Step;
import praktikum.stellar.client.base.Client;
import praktikum.stellar.model.ingredient.IngredientsResponse;

import static io.restassured.RestAssured.given;

public class IngredientClient extends Client {

    protected static final String INGREDIENT_URI = "ingredients";

    @Step("Get ingredients")
    public IngredientsResponse get() {
        return given().spec(getBaseSpec())
                .when()
                .get(INGREDIENT_URI)
                .as(IngredientsResponse.class);
    }
}
