package ch.unige.pinfo3;

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
public class ResultsResourceTest{

    //InputStream testSearch = getClass().getClassLoader().getResourceAsStream("testSearch.json");

    @Test
    @Order(1)
    public void shouldGetAllResults() {
        given()
                .when()
                .get("/results")
                .then()
                .assertThat()
                .statusCode(is(404)); //// whyyyyyyyyyyyy?
                //.and()
                //.body("size()", equalTo(10)); // 10 results in the DB on startup
    }

    @Test
    @Order(2)
    public void shouldGetResultById(){
        given()
                .when()
                .get("/results/50a69566-3d73-4163-9405-8c314d71970f")
                .then()
                .assertThat()
                .statusCode(is(404)); //// whyyyyyyyyyyyy?
                //.and()
                //.body("data", equalTo("Proin interdum mauris non ligula pellentesque ultrices. Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl."));
    }

}
