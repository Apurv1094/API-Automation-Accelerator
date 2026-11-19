package com.accelerator.tests;

import com.accelerator.base.BaseTest;
import com.accelerator.utils.TestDataFactory;
import com.accelerator.utils.TestDataReader;
import com.accelerator.validation.ResponseValidator;
import com.accelerator.validation.SchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class ProductApiTest extends BaseTest {
    @Test(groups = { "smoke", "regression" })
    public void getProductById() {
        Response r = apiClient.get("/products/{id}", Map.of("id", 1), Map.of());
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "id", 1);
    }

    @Test(groups = "regression")
    public void productHasTitle() {
        ResponseValidator.bodyMatches(apiClient.get("/products/{id}", Map.of("id", 1), Map.of()), "title",
                not(emptyOrNullString()));
    }

    @Test(groups = "regression")
    public void productHasPrice() {
        ResponseValidator.bodyMatches(apiClient.get("/products/{id}", Map.of("id", 1), Map.of()), "price",
                greaterThan(0));
    }

    @Test(groups = "regression")
    public void productHasCategory() {
        ResponseValidator.bodyMatches(apiClient.get("/products/{id}", Map.of("id", 1), Map.of()), "category",
                not(emptyOrNullString()));
    }

    @Test(groups = "regression")
    public void listProducts() {
        Response r = apiClient.get("/products");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "products.size()", greaterThan(0));
    }

    @Test(groups = "regression")
    public void productPagination() {
        Response r = apiClient.get("/products", Map.of("limit", 5, "skip", 0));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "products.size()", 5);
    }

    @Test(groups = "regression")
    public void productSkip() {
        Response r = apiClient.get("/products", Map.of("limit", 5, "skip", 5));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "skip", 5);
    }

    @Test(groups = "regression")
    public void productSearch() {
        Response r = apiClient.get("/products/search", Map.of("q", "phone"));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "products.size()", greaterThan(0));
    }

    @Test(groups = "regression")
    public void productCategories() {
        ResponseValidator.statusCode(apiClient.get("/products/categories"), 200);
    }

    @Test(groups = "regression")
    public void productsByCategory() {
        Response r = apiClient.get("/products/category/{category}", Map.of("category", "smartphones"), Map.of());
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "products.size()", greaterThan(0));
    }

    @Test(groups = "negative")
    public void missingProductReturns404() {
        ResponseValidator.statusCode(apiClient.get("/products/{id}", Map.of("id", 999999), Map.of()), 404);
    }

    @Test(groups = "regression")
    public void createProduct() {
        Map<String, Object> body = TestDataReader.getData("products.json", "createProduct");
        Response r = apiClient.post("/products/add", body);
        ResponseValidator.statusCode(r, 201);
        ResponseValidator.bodyEquals(r, "title", body.get("title"));
    }

    @Test(groups = "regression")
    public void updateProduct() {
        Response r = apiClient.put("/products/1", TestDataReader.getData("products.json", "updateProduct"));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "title", "Updated Laptop");
    }

    @Test(groups = "regression")
    public void patchProduct() {
        Response r = apiClient.patch("/products/1", TestDataReader.getData("products.json", "patchProduct"));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "price", 123);
    }

    @Test(groups = "regression")
    public void deleteProduct() {
        Response r = apiClient.delete("/products/1");
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "isDeleted", true);
    }

    @Test(groups = "regression")
    public void productSchema() {
        Response r = apiClient.get("/products/{id}", Map.of("id", 1), Map.of());
        ResponseValidator.statusCode(r, 200);
        SchemaValidator.validate(r, "schemas/product.json");
    }

    @org.testng.annotations.DataProvider(name = "productIds")
    public Object[][] productIds() {
        return new Object[][] { { 1, 200 }, { 2, 200 }, { 999999, 404 } };
    }

    @Test(dataProvider = "productIds", groups = { "regression", "negative" })
    public void productIdDataDriven(int id, int expectedStatus) {
        Response response = apiClient.get("/products/{id}", Map.of("id", id), Map.of());
        ResponseValidator.statusCode(response, expectedStatus);
    }

    @Test(groups = "regression")
    public void categoryWithPagination() {
        Response r = apiClient.get("/products/category/{category}",
                Map.of("category", "smartphones"), Map.of("limit", 2, "skip", 0));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyEquals(r, "products.size()", 2);
    }
}
