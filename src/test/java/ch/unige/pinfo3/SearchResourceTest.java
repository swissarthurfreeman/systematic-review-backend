package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.logging.Log;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.*;

import static io.quarkus.test.keycloak.server.KeycloakTestResourceLifecycleManager.getAccessToken;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.testcontainers.shaded.org.hamcrest.CoreMatchers.equalTo;


@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchResourceTest{

    @Inject
    EntityManager em;

    @InjectMock
    MockJobService mockJobService;

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    Job job = JobService.getRandomJob();

    String getElementFromJson(String json, String element){
        JSONObject obj = new JSONObject(json);
        return (obj.getString(element));
    }

    /*

    @Test
    @Order(4)
    void testMutualExclusionJobResult(){
        // this test is wrong
    
        String[] jobs;
        String[] results;

        jobs = get("/searches").body().jsonPath().getObject("job_uuid",String[].class );
        results = get("/searches").body().jsonPath().getObject("result_uuid",String[].class );

        for(int i = 0; i < jobs.length; i++){
            if(jobs[i] != null) {
                Assertions.assertNull(results[i]);
            }
            else{
                Assertions.assertNotNull(results[i]);
            }
        }

        // TODO these are unit tests, They should go elsewhere
        Search search = new Search();
        search.setJobUUID("f464a099-e489-939f-9499-c04a371dvd93");
        Assertions.assertNotNull(search.getJobUUID());
        Assertions.assertNull(search.getResultUUID());
        search.setResultUUID("jd9e4jf8-e489-939f-9499-meif932j4ns9");
        Assertions.assertNotNull(search.getResultUUID());
        Assertions.assertNull(search.getJobUUID());


    }



    @Test
    @Order(2)
    //verifie le nb d'attributs pour un search, et les attributs pour un search test
    void shouldGetSearchById(){
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches?uuid=c044a099-e489-43f8-9499-c04a371dbb65")
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

    // Test endpoint POST /searches
    @Test
    @Order(1)
    @TestTransaction
    void postSearch(){
        Log.info("Test endpoint POST /searches");
        //Search search = SearchService.getRandomSearch(testUsers[testUsers.length-1].uuid, null, UUID.randomUUID().toString());
        //SearchService.create(search);
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


    // Post a search, that is stored in a variable, test getElementFromJson
    @Order(2)
    @Test
    void AddSearch(){
        Log.info("Post a search, that is stored in a variable, test getElementFromJson");
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        String testSearchJson = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches").getBody().asString();

        Assertions.assertEquals("hiv AND covid AND ebola", getElementFromJson(testSearchJson, "query"));
    }


    @Test
    @Order(3)
    @Transactional
    void PostInvalidShortSearch(){
        Log.info("Testing POST /searches with too short query");
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

        Log.info("Testing GET /searches");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(2));

    }

    // Testing POST /user/:id/searches with invalid characters
    /// todo ATTENTION: Pour le moment il est possible de poster des recherches qui sont syntaxiquement incorrectes
    @Test
    @Order(4)
    @Transactional
    void PostInvalidSearch(){
        Log.info("Testing POST /user/:id/searches with invalid characters");
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


        Log.info("Testing GET /user/:id/searches");
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(3));
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
                .body("size()", CoreMatchers.equalTo(3));
    }

    // Testing GET /searches/:id
    @Test
    @Order(6)
    void getSpecificSearch(){
        Log.info("Testing GET /searches/:id");

        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        String testSearchJson = given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches").getBody().asString();
        Log.info(testSearchJson);


        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/searches/"+getElementFromJson(testSearchJson, "uuid"))
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(7));
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }





}


