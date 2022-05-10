package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.JobService;
import com.github.javafaker.Faker;
import io.quarkus.logging.Log;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.UUID;

import static ch.unige.pinfo3.domain.service.UserService.getRandomUser;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest{

    @Inject
    EntityManager em;

    @InjectMock
    MockJobService mockJobService;

    Job job = JobService.getRandomJob();

    InputStream testUser = getClass().getClassLoader().getResourceAsStream("testUser.json");

    // verifier les type de UUID
    // verifier ce qui se pase si je donne, si je donne pas, sie je donne la merde, etc...
    // verifier si je donne un UUID existant

    /// TODO une fois tests passent, faire des tests qui ne passent pas pour voir les codes d'erreur

    static User[] testUsers = new User[10];

    String getElementFromJson(String json, String element){
        JSONObject obj = new JSONObject(json);
        return (obj.getString(element));
    }

    @BeforeAll
    static void logStatus(){
        Log.info("Testing /users endpoint");
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
            .body("size()", equalTo(testUsers.length)); // verifie si les 10 users ont bien été persisté dans la bd
        Log.info((get("/users").body()).toString());
    }

    // Test endpoint GET /users/id with existing user
    @Test
    @Order(3)
    void verifyUser(){
        Log.info("Test endpoint GET /users/id with existing user");
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

    //Test endpoint GET /users/id with not existing user
    @Test
    @Order(4)
    void getNotExistingUser(){
        Log.info("Test endpoint GET /users/id with not existing user");
        given()
                .when()
                .get("/users/0")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    //Test endpoint POST /users with already existing username
    @Test
    @Order(5)
    void postExistingUserName(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with already existing username");
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

    //Test endpoint POST /users with already existing email
    @Test
    @Order(6)
    void postExistingUserEmail(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with already existing email");
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

    //Test endpoint POST /users with already existing uuid
    @Test
    @Order(7)
    void postExistingUserUUID(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with already existing uuid");
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

    // Test endpoint POST /users with invalid username
    @Test
    @Order(8)
    void postInvalidUserName(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with invalid username");
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

    // Test endpoint POST /users with invalid email
    @Test
    @Order(9)
    void postInvalidUserEmail(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with invalid email");
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

    // Test endpoint POST /users with invalid uuid
    @Test
    @Order(10)
    void postInvalidUserUUID(){
        Faker fk = new Faker();
        Log.info("Test endpoint POST /users with invalid uuid");
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

    // Test endpoint PUT /users with new user
    @Test
    @Order(11)
    void putNewUser(){
        Faker fk = new Faker();
        Log.info("Test endpoint PUT /users with new user");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" ,fk.name().username(), fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+ UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(is(200));

        given()
                .when()
                .get("/users")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat()
                .body("size()", equalTo(testUsers.length+1));
    }

    //Test endpoint PUT /users with existing user, changing the username
    @Test
    @Order(12)
    void putUserName(){
        Faker fk = new Faker();
        Log.info("Test endpoint PUT /users with existing user, changing the username");
        String newUsername = fk.name().username();
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" , newUsername, testUsers[testUsers.length-1].email);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+testUsers[testUsers.length-1].uuid)
                .then()
                .assertThat()
                .statusCode(is(200));

        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid)
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat()
                .body("username", equalTo(newUsername))
                .and()
                .assertThat()
                .body("email", equalTo(testUsers[testUsers.length-1].email));

        // update in testUsers table
        testUsers[testUsers.length-1].username = newUsername;
    }

    //Test endpoint PUT /users with existing user, changing the email
    @Test
    @Order(13)
    void putUserEmail(){
        Faker fk = new Faker();
        Log.info("Test endpoint PUT /users with existing user, changing the email");
        String newEmail = fk.internet().emailAddress();
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" , testUsers[testUsers.length-1].username, newEmail);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+testUsers[testUsers.length-1].uuid)
                .then()
                .assertThat()
                .statusCode(is(200));

        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid)
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat()
                .body("username", equalTo(testUsers[testUsers.length-1].username))
                .and()
                .assertThat()
                .body("email", equalTo(newEmail));

        // update in testUsers table
        testUsers[testUsers.length-1].email = newEmail;
    }

    // Test endpoint PUT /users with new user, with invalid username
    @Test
    @Order(14)
    void putNewUserInvalidUsername(){
        Faker fk = new Faker();
        Log.info("Test endpoint PUT /users with new user, with invalid username");
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" ,"kdé$éd£aàs_dàsa" , fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Test endpoint PUT /users with new user, with invalid email
    @Test
    @Order(15)
    void putNewUserInvalidEmail(){
        Log.info("Test endpoint PUT /users with new user, with invalid email");
        Faker fk = new Faker();
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" ,fk.name().username() , "dàpàkoèchU(&√@gmail.com");
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Test endpoint PUT /users with new user, with invalid email
    @Test
    @Order(16)
    void putNewUserInvalidUUID(){
        Log.info("Test endpoint PUT /users with new user, with invalid email");
        Faker fk = new Faker();
        String userJson = String.format("{\"username\": \"%s\", \"email\": \"%s\"}" ,fk.name().username() , fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/12¬34$ªœ")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Test endpoint PUT /users with new user, with invalid body
    @Test
    @Order(17)
    void putNewUserInvalidBody(){
        Log.info("Test endpoint PUT /users with new user, with invalid body");
        Faker fk = new Faker();
        String userJson = String.format("{\"username\": \"%s\", \"e-mail\": \"%s\"}" ,fk.name().username() , fk.internet().emailAddress());
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .when()
                .put("/users/"+UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(is(400));
    }


    // Test endpoint POST /user/:id/searches
    @Test
    @Order(18)
    @TestTransaction
    void postSearch(){
        Log.info("Test endpoint POST /user/:id/searches");
        //Search search = SearchService.getRandomSearch(testUsers[testUsers.length-1].uuid, null, UUID.randomUUID().toString());
        //SearchService.create(search);
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");

        Log.info("Testing POST /user/:id/searches");
        Log.info(searchJson);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(200));

    }


    // Post a search, that is stored in a variable, test getElementFromJson
    @Order(19)
    @Test
    void AddSearch(){
        Log.info("Post a search, that is stored in a variable, test getElementFromJson");
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        String testSearchJson = given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/"+testUsers[testUsers.length-1].uuid+"/searches").getBody().asString();

        Assertions.assertEquals("hiv AND covid AND ebola", getElementFromJson(testSearchJson, "query"));
    }


    @Test
    @Order(20)
    @Transactional
    void PostInvalidShortSearch(){
        Log.info("Testing POST /user/:id/searches with too short query");
        String searchJson = ("{\"query\": \"hiv\"}");
        Log.info(searchJson);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(400));

        Log.info("Testing GET /user/:id/searches");
        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(2));

    }

    // Testing POST /user/:id/searches with invalid characters
    /// todo ATTENTION: Pour le moment il est possible de poster des recherches qui sont syntaxiquement incorrectes
    @Test
    @Order(21)
    @Transactional
    void PostInvalidSearch(){
        Log.info("Testing POST /user/:id/searches with invalid characters");
        String searchJson = ("{\"query\": \"ifféie%oiem_$~¡§π«\"}");
        Log.info(searchJson);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(200));


        Log.info("Testing GET /user/:id/searches");
        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(3));
    }

    // Testing POST /user/:id/search with inexiatant id
    @Test
    @Order(22)
    @TestTransaction
    void postInexistantUserSearch(){
        Log.info("Testing POST /user/:id/search with inexiatant id");
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");

        Log.info("Testing POST /user/:id/searches");
        Log.info(searchJson);
        given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/1234/searches")
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    // Testing GET /users/:id/searches
    @Test
    @Order(23)
    void getSearches(){
        Log.info("Testing GET /user/:id/searches");
        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid+"/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(3));
    }

    // Testing GET /users/:id/searches/:id
    @Test
    @Order(24)
    void getSpecificSearch(){
        Log.info("Testing GET /user/:id/searches/:id");

        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        String testSearchJson = given()
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/users/"+testUsers[testUsers.length-1].uuid+"/searches").getBody().asString();
        Log.info(testSearchJson);


        given()
                .when()
                .get("/users/"+testUsers[testUsers.length-1].uuid+"/searches/"+getElementFromJson(testSearchJson, "uuid"))
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(7));
    }

    //Testing GET /user/:id/searches/:id with inexistent user
    @Test
    @Order(25)
    void getSpcificSearchInexistantUser(){
        Log.info("Testing GET /user/:id/searches/:id with inexistent user");
        given()
                .when()
                .get("/users/1234/searches/" + UUID.randomUUID())
                .then()
                .assertThat()
                .statusCode(is(400));
    }

    @Order(26)
    @Test
    @Transactional
    void persistResult() {
        Log.info("persisting a results and articles to DB");
        Log.info(em);
        em.persist(job);
    }

    // Testing GET /user/:id/jobs
    @Test
    @Order(26)
    @TestTransaction
    void getJobsOfUser(){
        Log.info("Testing GET /user/:id/jobs");
        //when(mockJobService.submit("hiv AND covid AND ebola")).thenReturn("908e5224-c74c-4ffd-bc45-9ef0b95462aa");
        //String jobUUID =  mockJobService.submit("hiv AND covid AND ebola");
        given()
                .when()
                .get("users/"+testUsers[testUsers.length-1].uuid+"/jobs")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .assertThat()
                .body("size()", equalTo(0)); /// todo: 0 pour que tests passent. Voir comment ajouter job à utilisateur
    }

    // Testing GET /user/:id/jobs for inexistant user
    @Test
    @Order(27)
    @TestTransaction
    void getJobsOfInexistantUser(){
        Log.info("Testing GET /user/:id/jobs");
        when(mockJobService.submit("hiv AND covid AND ebola")).thenReturn("908e5224-c74c-4ffd-bc45-9ef0b95462aa");
        String jobUUID =  mockJobService.submit("hiv AND covid AND ebola");
        given()
                .when()
                .get("users/1234/jobs")
                .then()
                .assertThat()
                .statusCode(is(400))
                .and()
                .assertThat()
                .statusLine(String.valueOf("HTTP/1.1 400 Bad Request"));
    }



    // test si tous les emails sont valides
    @Test
    void testEmails(){
        Log.info("User email verification");
        String[] emails;
        emails = get("/users").body().jsonPath().getObject("email",String[].class );
        for(String e: emails){
            Assertions.assertTrue(EmailValidator.getInstance().isValid(e));
        }
    }

}
