package com.accelerator.tests;

import com.accelerator.base.BaseTest;
import com.accelerator.utils.TestDataReader;
import com.accelerator.utils.TestDataFactory;
import com.accelerator.validation.ResponseValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class UserApiTest extends BaseTest {
    @Test(groups = { "smoke", "regression" })
    public void getUser() {
        Response r = apiClient.get("/users/1");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "id", 1);
    }

    @Test(groups = "regression")
    public void userHasFirstName() {
        ResponseValidator.bodyMatches(apiClient.get("/users/1"), "firstName", not(emptyOrNullString()));
    }

    @Test(groups = "regression")
    public void listUsers() {
        Response r = apiClient.get("/users");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "users.size()", greaterThan(0));
    }

    @Test(groups = "regression")
    public void paginateUsers() {
        Response r = apiClient.get("/users", Map.of("limit", 3));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "users.size()", 3);
    }

    @Test(groups = "regression")
    public void searchUsers() {
        ResponseValidator.statusCode(apiClient.get("/users/search", Map.of("q", "John")), 200);
    }

    @Test(groups = "negative")
    public void unknownUserReturns404() {
        ResponseValidator.statusCode(apiClient.get("/users/999999"), 404);
    }

    @Test(groups = "regression")
    public void createUser() {
        Map<String, Object> body = TestDataFactory.user();
        Response r = apiClient.post("/users/add", body);
        ResponseValidator.statusCode(r, 201);
        ResponseValidator.bodyEquals(r, "firstName", body.get("firstName"));
    }

    @Test(groups = "regression")
    public void updateUser() {
        Response r = apiClient.put("/users/1", Map.of("firstName", "Updated"));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "firstName", "Updated");
    }

    @Test(groups = "regression")
    public void deleteUser() {
        Response r = apiClient.delete("/users/1");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "isDeleted", true);
    }
}
