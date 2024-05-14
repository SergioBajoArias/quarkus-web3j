package com.xeridia;

import com.xeridia.resource.MintRequest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class ERC20ResourceTest {

    @Test
    void testMint() {
        given()
            .when()
                .get("/erc20/balanceOf")
            .then()
                .statusCode(200)
                .body(is("0"));

        given()
            .with()
                .body(MintRequest.builder().amount(10).build())
                .contentType(ContentType.JSON)
            .when()
                .post("/erc20/mint")
            .then()
                .statusCode(200);

        given()
            .when()
                .get("/erc20/balanceOf")
            .then()
                .statusCode(200)
                .body(is("10"));
    }

}