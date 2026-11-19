package com.accelerator.client;

import com.accelerator.request.RequestSpecFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;

/** Generic REST client: no product-specific paths or payloads. */
public class ApiClient {
    private final Map<String, String> headers;

    public ApiClient() {
        this(Map.of());
    }

    private ApiClient(Map<String, String> headers) {
        this.headers = Map.copyOf(headers);
    }

    public ApiClient withHeader(String name, String value) {
        Map<String, String> copy = new HashMap<>(headers);
        copy.put(name, value);
        return new ApiClient(copy);
    }

    public ApiClient withBearerToken(String token) {
        return withHeader("Authorization", "Bearer " + token);
    }

    public ApiClient withApiKey(String headerName, String key) {
        return withHeader(headerName, key);
    }

    private RequestSpecification request(Map<String, ?> path, Map<String, ?> query) {
        return RestAssured.given().spec(RequestSpecFactory.create(headers))
                .pathParams(path).queryParams(query);
    }

    public Response get(String endpoint) {
        return get(endpoint, Map.of(), Map.of());
    }

    public Response get(String endpoint, Map<String, ?> query) {
        return get(endpoint, Map.of(), query);
    }

    public Response get(String endpoint, Map<String, ?> path, Map<String, ?> query) {
        return request(path, query).get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return post(endpoint, body, Map.of(), Map.of());
    }

    public Response post(String endpoint, Object body, Map<String, ?> path, Map<String, ?> query) {
        return request(path, query).body(body).post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return put(endpoint, body, Map.of());
    }

    public Response put(String endpoint, Object body, Map<String, ?> path) {
        return request(path, Map.of()).body(body).put(endpoint);
    }

    public Response patch(String endpoint, Object body) {
        return patch(endpoint, body, Map.of());
    }

    public Response patch(String endpoint, Object body, Map<String, ?> path) {
        return request(path, Map.of()).body(body).patch(endpoint);
    }

    public Response delete(String endpoint) {
        return delete(endpoint, Map.of());
    }

    public Response delete(String endpoint, Map<String, ?> path) {
        return request(path, Map.of()).delete(endpoint);
    }
}
