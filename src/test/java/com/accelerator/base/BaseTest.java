package com.accelerator.base;

import com.accelerator.client.ApiClient;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected ApiClient apiClient;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        apiClient = new ApiClient();
    }
}
