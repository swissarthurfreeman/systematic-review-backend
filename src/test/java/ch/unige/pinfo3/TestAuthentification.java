package ch.unige.pinfo3;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestAuthentification {
    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Test
    public void testAdminAccess() {
        given().auth().oauth2(getAccessToken("alice"))
                .when().get("/users")
                .then()
                .statusCode(200);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
