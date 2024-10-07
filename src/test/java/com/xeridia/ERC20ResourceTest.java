package com.xeridia;

import com.xeridia.resource.BurnRequest;
import com.xeridia.resource.MintRequest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.integers;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class ERC20ResourceTest {

    @TestHTTPResource("/")
    URL baseURL;

    static HttpClient client;

    static ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll() {
        client = HttpClient.newBuilder().build();

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @AfterAll
    public static void afterAll() {
        if(client != null) {
            client.close();
        }
    }

    @Test
    void testMint() {
        int amount = 10;

        given()
            .when()
                .get("/erc20/balanceOf")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("0"));

        given()
            .with()
                .body(MintRequest.builder().amount(amount).build())
                .contentType(ContentType.JSON)
            .when()
                .post("/erc20/mint")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("transactionHash", startsWith("0x"))
                .body("gasUsed", Matchers.greaterThan(0));

        given()
            .when()
                .get("/erc20/balanceOf")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is(String.valueOf(amount)));

        given()
            .with()
                .body(BurnRequest.builder().amount(amount + 1).build())
                .contentType(ContentType.JSON)
            .when()
                .post("/erc20/burn")
            .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        given()
            .with()
                .body(BurnRequest.builder().amount(amount).build())
                .contentType(ContentType.JSON)
            .when()
                .post("/erc20/burn")
            .then()
                .statusCode(HttpStatus.SC_OK)
                .body("transactionHash", startsWith("0x"))
                .body("gasUsed", Matchers.greaterThan(0));

        given()
                .when()
                .get("/erc20/balanceOf")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("0"));
    }

    @Test
    void testMintPBT() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        qt()
            .forAll(integers().allPositive())
            .checkAssert((amount) -> {
                try {
                    testRequest("/erc20/balanceOf", String.valueOf(0));

                    testTransaction("/erc20/mint", amount, HttpStatus.SC_OK);
                    testRequest("/erc20/balanceOf", String.valueOf(amount));

                    if(amount < Integer.MAX_VALUE) {
                        testTransaction("/erc20/burn", amount + 1, HttpStatus.SC_BAD_REQUEST);
                        testRequest("/erc20/balanceOf", String.valueOf(amount));
                    }

                    testTransaction("/erc20/burn", amount, HttpStatus.SC_OK);
                    testRequest("/erc20/balanceOf", String.valueOf(0));
                } catch (URISyntaxException | InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private void testRequest(String endpoint, String expectedOutput) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL.toString() + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(HttpStatus.SC_OK, response.statusCode());
        Assertions.assertEquals(expectedOutput, response.body());
    }

    private void testTransaction(String endpoint, Integer amount, int expectedCode) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL.toString() + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(amount)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(expectedCode, response.statusCode());
        if(expectedCode == HttpStatus.SC_OK) {
            TransactionReceipt transactionReceipt = objectMapper.readValue(response.body(), TransactionReceipt.class);
            Assertions.assertTrue(transactionReceipt.getTransactionHash().startsWith("0x"));
            Assertions.assertTrue(transactionReceipt.getGasUsed().compareTo(BigInteger.ZERO) > 0);
        }
    }

}