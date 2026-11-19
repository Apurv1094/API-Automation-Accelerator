package com.accelerator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;

/** Loads named request payloads from classpath JSON fixtures. */
public final class TestDataReader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataReader() {
    }

    public static Map<String, Object> getData(String file, String key) {
        if (!file.matches("[a-zA-Z0-9_-]+\\.json"))
            throw new IllegalArgumentException("Use a JSON fixture filename only");
        try (InputStream in = TestDataReader.class.getClassLoader()
                .getResourceAsStream("testdata/" + file)) {
            if (in == null)
                throw new IllegalArgumentException("Missing testdata/" + file);
            Map<String, Object> entries = MAPPER.readValue(in, new TypeReference<>() {
            });
            Object value = entries.get(key);
            if (!(value instanceof Map<?, ?>))
                throw new IllegalArgumentException("Missing object payload '" + key + "' in " + file);
            return MAPPER.convertValue(value, new TypeReference<Map<String, Object>>() {
            });
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot read fixture: " + file, e);
        }
    }
}
