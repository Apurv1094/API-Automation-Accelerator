package com.accelerator.utils;

import java.util.Map;
import java.util.UUID;

public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static String uniqueName(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static Map<String, Object> product() {
        return Map.of("title", uniqueName("TestProduct"), "price", 99);
    }

    public static Map<String, Object> user() {
        return Map.of("firstName", uniqueName("QA"), "lastName", "Automation", "age", 29);
    }
}
