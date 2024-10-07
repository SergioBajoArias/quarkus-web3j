package com.xeridia;

import com.xeridia.resource.BurnRequest;
import com.xeridia.resource.MintRequest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.integers;

@QuarkusTest
@QuarkusTestResource(GanacheTestResourceLifecycleManager.class)
class ERC20ResourceTest {

    @Test
    void testMint() {
        performTestAndVerifications(10);
    }

    @Test
    void testMintPBT() {
        qt()
            .forAll(integers().allPositive())
            .checkAssert(ERC20ResourceTest::performTestAndVerifications);
    }

    private static void performTestAndVerifications(int amount) {
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

        if(amount < Integer.MAX_VALUE) {
            given()
                    .with()
                    .body(BurnRequest.builder().amount(amount + 1).build())
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/erc20/burn")
                    .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST);

            given()
                    .when()
                    .get("/erc20/balanceOf")
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body(is(String.valueOf(amount)));
        }

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
}