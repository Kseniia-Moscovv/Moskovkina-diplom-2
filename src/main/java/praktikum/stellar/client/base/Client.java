package praktikum.stellar.client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Client {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";

    protected static final RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }

}
