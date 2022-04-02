package ch.unige.pinfo3;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class JobResourceTest {
    @Test
    public void shouldGetAllJobs() {
        given()
            .when()
            .get("/jobs")
            .then()
            .assertThat()
            .statusCode(is(200))
                .and()
                .body("size()", equalTo(2)); // default test list has two jobs.
    }
}
