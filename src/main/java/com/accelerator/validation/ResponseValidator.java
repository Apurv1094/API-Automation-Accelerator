package com.accelerator.validation;

import io.restassured.response.Response;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public final class ResponseValidator {
    private ResponseValidator() {
    }

    public static void statusCode(Response response, int expected) {
        assertThat("HTTP status", response.statusCode(), equalTo(expected));
    }

    public static void bodyEquals(Response response, String jsonPath, Object expected) {
        assertThat("JSON path: " + jsonPath, response.jsonPath().get(jsonPath), equalTo(expected));
    }

    public static void bodyMatches(Response response, String jsonPath, Matcher<?> matcher) {
        response.then().body(jsonPath, matcher);
    }

    public static void headerEquals(Response response, String name, String expected) {
        assertThat("Header: " + name, response.header(name), equalTo(expected));
    }
}
