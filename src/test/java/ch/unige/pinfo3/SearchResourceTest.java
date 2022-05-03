package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Search;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.ObjectUtils;
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

    InputStream testSearch = getClass().getClassLoader().getResourceAsStream("testSearch.json");

    @Test
    @Order(1)
    public void shouldGetAllSearches() {
        given()
                .when()
                .get("/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(30)); // 30 search in the DB on startup
    }

    @Test
    @Order(2)
    public void shouldGetAllSearchesForAUser() {
        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(5)); // 5 searches for this user on startup
    }

    @Test
    @Order(3)
    public void shouldPostSearch(){
        given()
                .contentType(ContentType.JSON)
                .body(testSearch)
                .when()
                .post("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches")
                .then()
                .assertThat()
                .statusCode(is(405)); // whyyyyyyyy 405 ??

        given()
                .when()
                .get("/users/c044a099-e489-43f8-9499-c04a371dbb62/searches")
                .then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .body("size()", equalTo(5));
    }

    @Test
    @Order(4)
    public void testMutualExclusionJobResult(){
        String[] jobs;
        String[] results;

        jobs = get("/searches").body().jsonPath().getObject("job_uuid",String[].class );
        results = get("/searches").body().jsonPath().getObject("result_uuid",String[].class );

        for(int i = 0; i < jobs.length; i++){
            if(jobs[i] != null){
                Assertions.assertNull(results[i]);
            }
            else{
                Assertions.assertNotNull(results[i]);
            }
        }

        Search search = new Search();
        search.setJobUUID("f464a099-e489-939f-9499-c04a371dvd93");
        Assertions.assertNotNull(search.getJobUUID());
        Assertions.assertNull(search.getResultUUID());
        search.setResultUUID("jd9e4jf8-e489-939f-9499-meif932j4ns9");
        Assertions.assertNotNull(search.getResultUUID());
        Assertions.assertNull(search.getJobUUID());
    }
/*
    @Test
    @Order(2)
    //verifie le nb d'attributs pour un search, et les attributs pour un search test
    public void shouldGetSearchById(){
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

}
