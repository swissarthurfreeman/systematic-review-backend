package ch.unige.pinfo3;


import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest{

    File myFile = new File("/Users/bartalazar/Projet_info/backend/src/test/java/ch/unige/pinfo3/testUser.json");
    // avec chemin relative ce serait mieux

    @Test
    @Order(1)
    public void shouldGetAllUsers() {
        given()
            .when()
            .get("/users")
            .then()
            .assertThat()
            .statusCode(is(200))
            .and()
            .body("size()", equalTo(1)); // 1 user in the DB on startup
    }

    @Test
    @Order(2)
    //verifie le nb d'attributs pour un utilisateur, et les attributs pour un utilisateur test
    public void shouldGetUserById(){
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(3)) // il y a 3 attributs pour un utilisateur
                .and()
                .body("username", equalTo("LazarTest"))
                .and()
                .body("email", equalTo("test@lazar.com"));
    }

    @Test
    @Order(3)
    public void shouldDeleteUserById(){
        given()
                .when()
                .delete("/users/c044a099-e489-43f8-9499-c04a371dbb62")
                .then()
                .assertThat()
                .statusCode(is(405)); // un utilisateur ne peut pas être effacé
        /*
        given()
                .when()
                .delete("/users/c044a099-e489-43f8-9499-c04a371dbb62")
                .then()
                .assertThat()
                .statusCode(is(200));
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62")
                .then()
                .assertThat()
                .statusCode(is(201));

         */
    }


    @Test
    @Order(4)
    public void shouldPostUser(){
        given()
                .contentType(ContentType.JSON)
                .body(myFile)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(200)); // verifie si le post est fait avec succes
        System.out.println((get("/users").body()).getClass());
        System.out.println("-----------------------------------");
        Log.info("Log test!");

        given()
                .when()
                .get("/users/1ebf2120-40fb-462c-ad90-b6786b28c305")
                .then()
                .assertThat()
                .statusCode(is(204)); // pourquoi il ne trouve pas????

        given()
                .when()
                .get("/users")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(2)); // verifie qu'il y a bien 2 users dans la bd
    }

    @Test
    @Order(5)
    public void testAtInEmails(){

        String[] emails = new String[]{};

        emails = get("/users").body().jsonPath().getObject("email",String[].class );

        //System.out.println(emails[1]);

        for(String e: emails){
            Assertions.assertTrue(e.contains("@"));
        }
    }

    @Test
    @Order(6)
    public void testPointInEmails(){

        String[] emails = new String[]{};

        emails = get("/users").body().jsonPath().getObject("email",String[].class );

        for(String e: emails){
            Assertions.assertTrue(e.contains("."));
        }
    }



}
