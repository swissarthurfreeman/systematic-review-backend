package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.User;
import com.github.javafaker.Faker;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

import java.io.InputStream;
import java.util.UUID;

import static ch.unige.pinfo3.domain.service.UserService.getRandomUser;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest{

    InputStream testUser = getClass().getClassLoader().getResourceAsStream("testUser.json");

    // verifier les type de UUID
    // verifier ce qui se pase si je donne, si je donne pas, sie je donne la merde, etc...
    // verifier si je donne un UUID existant

    /// TODO une fois tests passent, faire des tests qui ne passent pas pour voir les codes d'erreur

    static User[] testUsers = new User[10];

    @BeforeAll
    static void logStatus(){
        Log.info("Testing /user endpoint");
    }

    @BeforeAll
    static void createTestUsers(){
        for(int i = 0; i < testUsers.length; i++){
            testUsers[i] = getRandomUser();
        }
    }

    // Test endpoint POST /users
    @Test
    @Order(1)
    void postUsers(){
        Log.info("Test endpoint POST /users");
        Log.info("Populating DB with users");
        for(int i = 0; i < testUsers.length-1; i++){
            User user = testUsers[i];
            String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}", user.username, user.email);
            given()
                    .when()
                    .contentType(ContentType.JSON)
                    .body(userJson)
                    .when()
                    .post("/users")
                    .then()
                    .assertThat()
                    .statusCode(is(200)); // verifie si le post est fait avec succes
        }
        String userJson = String.format("{\"uuid\": \"%s\", \"username\": \"%s\", \"email\": \"%s\"}", testUsers[testUsers.length-1].uuid, testUsers[testUsers.length-1].username, testUsers[testUsers.length-1].email);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(200)); // verifie si le post est fait avec succes
    }

    // Test endpoint GET /users
    @Test
    @Order(2)
    void getAllUsers() {
        Log.info("Test endpoint GET /users");
        given()
            .when()
            .get("/users")
            .then()
            .assertThat()
            .statusCode(is(200))
            .and()
            .body("size()", equalTo(10)); // verifie si les 10 users ont bien été persisté dans la bd
        Log.info((get("/users").body()).toString());
    }

    // Test endpoint GET /user/id with existing user
    @Test
    @Order(3)
    void verifyUser(){
        Log.info("Test endpoint GET /user/id with existing user");
        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid)
                .then()
                .assertThat()
                .statusCode(is(200))
                .body("size()", equalTo(3)) // il y a 3 attributs pour un utilisateur
                .and()
                .body("username", equalTo(testUsers[testUsers.length-1].username))
                .and()
                .body("email", equalTo(testUsers[testUsers.length-1].email));
    }

    //Test endpoint GET /user/id with not existing user
    @Test
    @Order(4)
    void getNotExistingUser(){
        Log.info("Test endpoint GET /user/id with not existing user");
        given()
                .when()
                .get("/users/0")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    //Test endpoint POST /user with already existing username
    @Test
    @Order(5)
    void postExistingUserName(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with already existing username");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}",testUsers[1].username, fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(409));
    }

    //Test endpoint POST /user with already existing email
    @Test
    @Order(6)
    void postExistingUserEmail(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with already existing email");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}",fk.name().username(), testUsers[1].email);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(409));
    }

    //Test endpoint POST /user with already existing uuid
    @Test
    @Order(7)
    void postExistingUserUUID(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with already existing uuid");
        String userJson = String.format("{\"uuid\": \"%s\", \"username\": \"%s\", \"email\": \"%s\"}", testUsers[testUsers.length-1].uuid ,fk.name().username(), fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(409));
    }

    // Test endpoint POST /user with invalid username
    @Test
    @Order(8)
    void postInvalidUserName(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with invalid username");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}","feédàep?fe!|wckowcm", fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Test endpoint POST /user with invalid email
    @Test
    @Order(9)
    void postInvalidUserEmail(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with invalid email");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}",fk.name().username(), "test[at]test.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Test endpoint POST /user with invalid uuid
    @Test
    @Order(10)
    void postInvalidUserUUID(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /user with invalid uuid");
        String userJson = String.format("{\"uuid\": \"%s\", \"username\": \"%s\", \"email\": \"%s\"}", "0123" ,fk.name().username(), fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(400));
    }


    @Test
    @Order(4)
    // test si tous les emails sont valides
    void testEmails(){
        Log.info("User email verification");
        String[] emails = new String[]{};
        emails = get("/users").body().jsonPath().getObject("email",String[].class );
        for(String e: emails){
            Assertions.assertTrue(EmailValidator.getInstance().isValid(e));
        }
    }

    /*
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

     */

    /*
    @Test
    @Order(3)
    public void shouldDeleteUserById(){
        given()
                .when()
                .delete("/users/c044a099-e489-43f8-9499-c04a371dbb62")
                .then()
                .assertThat()
                .statusCode(is(405)); // un utilisateur ne peut pas être effacé

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


    }
    */

    /*
    @Test
    @Order(4)
    public void shouldPostUser(){
        given()
                .contentType(ContentType.JSON)
                .body(testUser)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(is(200)); // verifie si le post est fait avec succes
        System.out.println((get("/users").body()).getClass());
        System.out.println("-----------------------------------");
        Log.info("Log test!");


        //Pas très utile pour le moment
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
                .body("size()", equalTo(31)); // verifie qu'il y a bien 31 users dans la bd
    }


     */


}
