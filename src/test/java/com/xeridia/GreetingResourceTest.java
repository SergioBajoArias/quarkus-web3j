package com.xeridia;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class GreetingResourceTest {

    private static final String CONTRACT_ADDRESS = "0xe78a0f7e598cc8b0bb87894b0f60dd2a88d6a8ab";

    @Test
    void testDeploy() {
        given()
                .when().post("/hello/blockchain")
                .then()
                .statusCode(200)
                .body(is(CONTRACT_ADDRESS));
    }

    @Test
    void testSayHello() {
        given()
                .when().get("/hello/blockchain/Sergio")
                .then()
                .statusCode(200)
                .body(is("Hello Sergio"));
    }

}