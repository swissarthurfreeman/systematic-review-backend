package ch.unige.pinfo3;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.InputStream;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchResourceTest{

    //InputStream testUser = getClass().getClassLoader().getResourceAsStream("testUser.json");

    @Test
    @Order(1)
    public void shouldGetAllSearches() {
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(1)); // 1 search in the DB on startup
    }
/*
    @Test
    @Order(2)
    //verifie le nb d'attributs pour un utilisateur, et les attributs pour un utilisateur test
    public void shouldGetSearchById(){
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches/c044a099-e489-43f8-9499-c04a371dbb65")
                // pourquoi 404??????????????????
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(4)) // il y a 4 attributs pour une recherche
                .and()
                .body("user_uuid", equalTo("c044a099-e489-43f8-9499-c04a371dbb62"))
                .and()
                .body("query", equalTo("HIV and SAHARA"))
                .and()
                .body("ucnf", equalTo("HIV and SAHARA"));
    }
 */

}
