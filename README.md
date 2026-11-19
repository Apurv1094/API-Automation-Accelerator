# API Testing Accelerator — Reusable Core + DummyJSON Example

Java 17, Maven, REST Assured, TestNG. **No JSON test plans, scenario IDs, or generic scenario evaluators.**

## Design

- `src/main/java/com/accelerator`: generic API client, request specs, configuration, token helper, validators, test data utilities.
- `src/test/java/com/accelerator/tests`: DummyJSON-specific test methods and expected results.
- `src/test/resources/testdata`: optional JSON **payloads only** (products/users/carts); no execution instructions.
- `src/test/resources/schemas`: JSON response contracts.
- `src/test/resources/config`: local/QA profiles.

Flow: TestNG test -> TestDataReader (optional) -> ApiClient -> RequestSpecFactory/ConfigManager -> REST Assured -> ResponseValidator/SchemaValidator -> Surefire/Jenkins.

## Run

Prerequisites: Java 17+ and Maven 3.9+. From project root:

```bash
mvn clean test
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
mvn clean test -Denv=qa
mvn clean test -Dbase.url=https://dummyjson.com
```

For protected DummyJSON login/profile tests set `API_USERNAME` and `API_PASSWORD` to valid DummyJSON demo credentials (do not commit secrets). Without them positive auth tests skip. Negative login does not require credentials.

Reports: `target/surefire-reports/`. CI examples: `Jenkinsfile`, `.github/workflows/api-tests.yml`.

## Request paths and parameters

```java
Response r = apiClient.get("/products/{id}", Map.of("id", 1), Map.of());
Response list = apiClient.get("/products", Map.of("limit", 5, "skip", 0));
```

## Payloads

```java
Map<String,Object> payload = TestDataReader.getData("products.json", "createProduct");
Response r = apiClient.post("/products/add", payload);
ResponseValidator.statusCode(r, 201);
```

For tiny payloads use `Map.of(...)` in tests; for dynamic values use `TestDataFactory`. For multiple expected inputs use TestNG `@DataProvider`. Never put test logic or expected statuses in fixture JSON.

## Authentication

`AuthTokens.getAccessToken()` reads username/password and login path/field names from config. `AuthTokens.authenticatedClient()` returns a bearer-authenticated `ApiClient`. For API-key services use `apiClient.withApiKey(headerName, key)`; for unsupported OAuth flows, implement a separate adapter rather than hardcoding service-specific behavior in core. No credentials or tokens should be committed.

## How to reuse for another API

1. Copy core `src/main/java/com/accelerator` (or publish as an internal Maven library).
2. Set base URL, timeouts, and auth settings in config or environment variables.
3. Replace DummyJSON test classes with new endpoint tests; adjust paths, query/path params, request bodies and expected results.
4. Replace payload fixtures and response schemas as needed; keep `ApiClient`, `RequestSpecFactory`, `ResponseValidator`, and `TestDataReader` unchanged for supported JSON REST APIs.
5. Configure CI secrets, run tests, inspect Surefire reports.

**Important:** DummyJSON POST/PUT/PATCH/DELETE operations are simulated; validate returned responses, not database persistence. Live API execution is not verified in this environment.
