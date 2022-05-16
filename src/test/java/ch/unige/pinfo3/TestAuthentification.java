package ch.unige.pinfo3;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.smallrye.jwt.build.Jwt;

import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TestAuthentification {
    // TODO : figure out a way to test the @Authentificated endpoints which will
    // work on the vm without launching a new container.
    KeycloakTestClient keycloakClient = new KeycloakTestClient();
    
    @Test
    public void testAdminAccess() {
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .when()
                .get("/searches")
                .then()
                .statusCode(200);
    }

    @Test
    public void testSearchPost() {
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");

        Log.info("Testing POST /user/:id/searches");
        Log.info(searchJson);
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches")
                .then()
                .assertThat()
                .statusCode(is(200));
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
