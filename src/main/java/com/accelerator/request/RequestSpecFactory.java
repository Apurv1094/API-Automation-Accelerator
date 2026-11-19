package com.accelerator.request;

import com.accelerator.config.ConfigManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public final class RequestSpecFactory {
    private RequestSpecFactory() {
    }

    public static RequestSpecification create() {
        return create(Map.of());
    }

    public static RequestSpecification create(Map<String, String> headers) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.require("base.url"))
                .setContentType(ContentType.JSON).setAccept(ContentType.JSON);
        int connect = Integer.parseInt(ConfigManager.getOrDefault("connect.timeout.ms", "10000"));
        int read = Integer.parseInt(ConfigManager.getOrDefault("read.timeout.ms", "20000"));
        builder.setConfig(io.restassured.config.RestAssuredConfig.config().httpClient(
                io.restassured.config.HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", connect)
                        .setParam("http.socket.timeout", read)));
        headers.forEach(builder::addHeader);
        return builder.build();
    }
}
