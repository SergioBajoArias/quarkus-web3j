package com.xeridia;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class GreetingResourceTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

    @Test
    void testDeploy() {
        given()
                .when().post("/hello/blockchain")
                .then()
                .statusCode(200)
                .body(is("Hello from Quarkus REST"));
    }

}