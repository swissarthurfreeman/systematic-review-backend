package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.utils.QueryUtils;
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
import java.util.List;

import static ch.unige.pinfo3.domain.service.JobService.getRandomJob;
import static ch.unige.pinfo3.domain.service.SearchService.getRandomSearch;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ApplicationScoped
class SearchResourceTest extends ResourceTestParent{

    Job job = getRandomJob();

    String getElementFromJson(String json, String element){
        JSONObject obj = new JSONObject(json);
        return (obj.getString(element));
    }

    static Search[] testSearches = new Search[9];

    @BeforeAll
    void setup(){
        for(int i = 0; i < 9; i++){
            testSearches[i] = getRandomSearch(userUUID, null, null);
            Search search = testSearches[i];
            // search for job or result with said ucnf
            List<Job> jobs = QueryUtils.select(Job.class, "ucnf", search.ucnf, em);
            List<Result> results = QueryUtils.select(Result.class, "ucnf", search.ucnf, em);

            if(!jobs.isEmpty()) {
                search.setJobUUID(jobs.get(0).uuid);
                search.setResultUUID(null);
            } else if(!results.isEmpty()) {
                search.setResultUUID(results.get(0).uuid);
                search.setJobUUID(null);
            } else {
                // submits job
                Job job = getRandomJob();
                job.ucnf = search.ucnf;
                em.persist(job);
                search.setJobUUID(job.uuid);
            }
        }
    }

    // Test endpoint POST /searches
    @Test
    @Order(1)
    @TestTransaction
    void postSearch(){
        Log.info("Test endpoint POST /searches");
        String searchJson = ("{\"query\": \"hiv AND covid AND ebola\"}");
        Log.info("Testing POST searches");
        Log.info(searchJson);
        String access_token = getAccessToken("alice");
        Log.info(access_token);
        given()
                .auth()
                .oauth2(access_token)
                .when()
                .contentType(ContentType.JSON)
                .body(searchJson)
                .when()
                .post("/searches")
                .then()
                .assertThat()
                .statusCode(is(200));
    }
    /*

    @Test
    @Order(2)
    //verifie le nb d'attributs pour un search, et les attributs pour un search test
    void shouldGetSearchById() {
        // TODO : Get sur /searches tu prends le premier search de la liste et tu récupère
        // son id et ENSUITE tu get /searches?uuid=id_recupere
        given()
                .when()
                .get("/searches?uuid=c044a099-e489-43f8-9499-c04a371dbb65")
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
                .post("/searches")
                .getBody()
                .asString();

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
}


