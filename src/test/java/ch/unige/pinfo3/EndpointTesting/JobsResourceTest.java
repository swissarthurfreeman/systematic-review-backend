package ch.unige.pinfo3.EndpointTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import lombok.ToString;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;

@ToString
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTestResource(OidcWiremockTestResource.class)
public class JobsResourceTest extends ResourceTestParent{

    Job job;

    public List<Job> jobs = new ArrayList<Job>();

    String jobUUID;


    @BeforeAll
    void Setup(){
        for(int i = 0; i < 10; i++){
            jobs.add(JobService.getRandomJob());
        }
        job = jobs.get(0);
    }

    @Test
    void GetSpecificJob(){

        jobUUID = job.uuid;

        Mockito.when(js.getJob(jobUUID)).thenReturn(Optional.ofNullable(job));

        Log.info(jobUUID);
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/jobs/"+ jobUUID)
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .body("size()", CoreMatchers.equalTo(4));
    }

    @Test
    void GetInexistingJob(){

        jobUUID = "1234";

        Log.info(jobUUID);
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/jobs/"+ jobUUID)
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(404))
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }

    // Testing GET /jobs
    @Test
    void getJobsOfUser(){
        Log.info("Testing GET /jobs");

        Mockito.when(js.getJobsOfUser(userUUID)).thenReturn(jobs);


        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/jobs")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(200))
                .and()
                .assertThat()
                .body("size()", CoreMatchers.equalTo(10));
    }
}
