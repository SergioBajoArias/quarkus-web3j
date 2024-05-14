package com.xeridia;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class ERC20ResourceTest {

    @Test
    void testSayHello() {
        given()
                .when().get("/erc20/balanceOf")
                .then()
                .statusCode(200)
                .body(is("0"));
    }

}