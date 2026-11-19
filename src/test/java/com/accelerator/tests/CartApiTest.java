package com.accelerator.tests;

import com.accelerator.base.BaseTest;
import com.accelerator.utils.TestDataReader;
import com.accelerator.validation.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.List;
import static org.hamcrest.Matchers.*;

public class CartApiTest extends BaseTest {
    @Test(groups = { "smoke", "regression" })
    public void getCart() {
        Response r = apiClient.get("/carts/1");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "id", 1);
    }

    @Test(groups = "regression")
    public void listCarts() {
        Response r = apiClient.get("/carts");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "carts.size()", greaterThan(0));
    }

    @Test(groups = "regression")
    public void getUserCarts() {
        ResponseValidator.statusCode(apiClient.get("/carts/user/5"), 200);
    }

    @Test(groups = "negative")
    public void missingCartReturns404() {
        ResponseValidator.statusCode(apiClient.get("/carts/999999"), 404);
    }

    @Test(groups = "regression")
    public void addCart() {
        Response r = apiClient.post("/carts/add",
                Map.of("userId", 1, "products", List.of(Map.of("id", 1, "quantity", 1))));
        ResponseValidator.statusCode(r, 201);
        ResponseValidator.bodyEquals(r, "userId", 1);
    }

    @Test(groups = "regression")
    public void updateCart() {
        Response r = apiClient.put("/carts/1",
                Map.of("merge", true, "products", List.of(Map.of("id", 1, "quantity", 2))));
        ResponseValidator.statusCode(r, 200);
    }

    @Test(groups = "regression")
    public void deleteCart() {
        Response r = apiClient.delete("/carts/1");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "isDeleted", true);
    }
}
