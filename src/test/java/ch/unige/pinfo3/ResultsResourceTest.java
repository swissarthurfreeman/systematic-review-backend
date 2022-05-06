package ch.unige.pinfo3;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
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
                .statusCode(is(200)); // this endpoint exists now :) 
                // TODO model of results has changed .sql file must be updated to reflect this.
                // honestly I'd just use faker and directly persist from Java before running the tests,
                // will be easier to maintain rather than a .sql file. See examples of usage of faker
                // in source code. 
                //.and()
                //.body("size()", equalTo(10)); 
    }

    @Test
    @Order(2)
    public void shouldGetResultById(){
        // find a way to avoid hard coding test data.
        given()
                .when()
                .get("/results/50a69566-3d73-4163-9405-8c314d71970f")
                .then()
                .assertThat()
                .statusCode(is(204)); // no content, doesn't exist for now.
                // TODO the Result model has changed and will have to be updated accordingly in the tests. 
                //.and()
                //.body("data", equalTo("Proin interdum mauris non ligula pellentesque ultrices. Phasellus id sapien in sapien iaculis congue. Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl."));
    }

}
