package com.accelerator.auth;

import com.accelerator.client.ApiClient;
import com.accelerator.config.ConfigManager;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic configurable username/password login helper; no DummyJSON-specific
 * endpoints or fields.
 */
public final class AuthTokens {
    private AuthTokens() {
    }

    public static boolean credentialsAvailable() {
        return !ConfigManager.get("api.username").isBlank() && !ConfigManager.get("api.password").isBlank();
    }

    public static String getAccessToken() {
        String user = ConfigManager.require("api.username");
        String pass = ConfigManager.require("api.password");
        Map<String, Object> payload = new HashMap<>();
        payload.put(ConfigManager.require("auth.username.field"), user);
        payload.put(ConfigManager.require("auth.password.field"), pass);
        Response response = new ApiClient().post(ConfigManager.require("auth.login.path"), payload);
        if (response.statusCode() < 200 || response.statusCode() >= 300)
            throw new IllegalStateException("Login failed: HTTP " + response.statusCode());
        String token = response.jsonPath().getString(ConfigManager.require("auth.token.field"));
        if (token == null || token.isBlank())
            throw new IllegalStateException("Login response did not include an access token");
        return token;
    }

    public static ApiClient authenticatedClient() {
        return new ApiClient().withBearerToken(getAccessToken());
    }
}
