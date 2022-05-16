package ch.unige.pinfo3;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TestAuthentification {

    @Test
    @TestSecurity(user = "alice", roles = "user")
    public void testAdminAccess() {
        given()
                .when()
                .get("/searches")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "alice", roles = "user")
    public void testSearchPost() {
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");

        Log.info("Testing POST /user/:id/searches");
        Log.info(searchJson);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches")
                .then()
                .assertThat()
                .statusCode(is(200));
    }
}
