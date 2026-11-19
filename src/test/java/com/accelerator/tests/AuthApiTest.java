package com.accelerator.tests;

import com.accelerator.auth.AuthTokens;
import com.accelerator.base.BaseTest;
import com.accelerator.config.ConfigManager;
import com.accelerator.validation.ResponseValidator;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.Test;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class AuthApiTest extends BaseTest {
    private void requireCredentials() {
        if (!AuthTokens.credentialsAvailable())
            throw new SkipException("Set API_USERNAME and API_PASSWORD to run auth tests");
    }

    @Test(groups = "auth")
    public void loginReturnsAccessToken() {
        requireCredentials();
        String token = AuthTokens.getAccessToken();
        org.testng.Assert.assertFalse(token.isBlank());
    }

    @Test(groups = "auth")
    public void authorizedProfile() {
        requireCredentials();
        Response r = AuthTokens.authenticatedClient().get(ConfigManager.require("auth.profile.path"));
        ResponseValidator.statusCode(r, 200);
        ResponseValidator.bodyMatches(r, "id", greaterThan(0));
    }

    @Test(groups = { "auth", "negative" })
    public void invalidLoginRejected() {
        Response r = apiClient.post(ConfigManager.require("auth.login.path"),
                Map.of(ConfigManager.require("auth.username.field"), "invalid-user-999",
                        ConfigManager.require("auth.password.field"), "invalid-password"));
        ResponseValidator.statusCode(r, 400);
    }
}
