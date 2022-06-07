package ch.unige.pinfo3.EndpointTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.utils.RandomProducer;
import io.quarkus.logging.Log;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ApplicationScoped
class SearchResourceTest extends ResourceTestParent{

    Job job = RandomProducer.getRandomJob();

    String getElementFromJson(String json, String element) {
        JSONObject obj = new JSONObject(json);
        return (obj.getString(element));
    }

    String searchBody;

    // Test endpoint POST /searches
    @Test
    @Order(1)
    @TestTransaction
    void postSearch() {
        Log.info("Test endpoint POST /searches");
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        Log.info("Testing POST searches");
        Log.info(searchJson);
        String access_token = getAccessToken("alice");
        Log.info(access_token);
        // Post a search, that is stored in a variable, test getElementFromJson
        searchBody = given()
                .auth()
                .oauth2(access_token)
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches")
                .getBody()
                .asString();

        for(int i = 0; i < 9; i++){
            Search search = RandomProducer.getRandomSearch(userUUID, UUID.randomUUID().toString(), null);
            Log.info(search.ucnf);
            given()
                    .auth()
                    .oauth2(access_token)
                    .when()
                    .contentType(ContentType.JSON)
                    .body("{\"query\": \" " + search.ucnf + " \"}")
                    .when()
                    .post("/searches")
                    .then()
                    .assertThat()
                    .statusCode(is(200));
        }
    }

    /// Todo Sert à rien, enlever. Fait la même chohse que GET /searches
    /*
    @Test
    @Order(2)
    //verifie le nb d'attributs pour un search, et les attributs pour un search test
    void shouldGetSearchById() {
        // TODO : Get sur /searches tu prends le premier search de la liste et tu récupère
        // son id et ENSUITE tu get /searches?uuid=id_recupere
        String access_token = getAccessToken("alice");

        // voir à quoi le body ressemble
        String body = given()
                .auth()
                .oauth2(access_token)
                .when()
                .get("/searches?uuid=" + getElementFromJson(searchBody, "uuid"))
                .getBody()
                .asPrettyString();
        Log.info(getElementFromJson(searchBody, "uuid"));
        Log.info(body);


        given()
                .auth()
                .oauth2(access_token)
                .when()
                .get("/searches?uuid=" + getElementFromJson(searchBody, "uuid"))
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(4)) // il y a 4 attributs pour une recherche
                .and()
                .body("user_uuid", CoreMatchers.equalTo(getElementFromJson(searchBody, "uuid")))
                .and()
                .body("query", CoreMatchers.equalTo(getElementFromJson(searchBody, "query")))
                .and()
                .body("ucnf", CoreMatchers.equalTo(getElementFromJson(searchBody, "ucnf")));
    }
     */


    @Test
    @Order(3)
    @Transactional
    void PostInvalidShortSearch(){
        /*Log.info("Testing POST /searches with too short query");
        String searchJson = ("{\"query\": \"hiv\"}");
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
                .statusCode(is(400));
        */
        
        Log.info("Testing GET /searches");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(10));
    }

    // Testing POST /user/:id/searches with invalid characters
    /// todo ATTENTION: Pour le moment il est possible de poster des recherches qui sont syntaxiquement incorrectes
    @Test
    @Order(4)
    @Transactional
    void PostInvalidSearch(){
        Log.info("Testing POST /searches with invalid characters");
        String searchJson = ("{\"query\": \"ifféie%oiem_$~¡§π«\"}");
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


        Log.info("Testing GET /searches");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(11));
    }

    // Testing GET /searches
    @Test
    @Order(5)
    void getSearches(){
        Log.info("Testing GET /searches");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(11));
    }

    // Testing GET /searches/:id
    @Test
    @Order(6)
    void getSpecificSearch(){
        Log.info("Testing GET /searches/:id");

        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches/"+getElementFromJson(searchBody, "uuid"))
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(7));
    }

    // Testing GET /searches/:id with invalid id
    @Test
    @Order(7)
    void getInexistantSearch(){
        Log.info("Testing GET /searches/:id with invalid id");

        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches/"+UUID.randomUUID().toString())
                .then()
                .assertThat()
                .statusCode(is(400));
    }
}


