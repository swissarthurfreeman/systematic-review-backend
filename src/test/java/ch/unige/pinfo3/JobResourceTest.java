package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.User;
import ch.unige.pinfo3.domain.service.JobService;
import com.github.javafaker.Faker;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;
import org.apache.commons.validator.routines.EmailValidator;
//import org.gradle.internal.impldep.javax.inject.Inject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;
import javax.inject.Inject;

import static ch.unige.pinfo3.domain.service.UserService.getRandomUser;
import static ch.unige.pinfo3.domain.service.JobService.getRandomJob;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

import io.quarkus.test.TestTransaction;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobResourceTest {
    /// en attente pour le développement de l'API
    //tests sur les jobs

    @InjectMock
    MockJobService mockJobService;

    @Test
    @Order(1)
    @TestTransaction
    void getJob() throws FileNotFoundException {

        String jobUUID =  mockJobService.submit("hiv AND covid AND ebola");

        Log.info(jobUUID);

        //String jobId = JobService.submit("hiv AND malaria");

        String body = given()
                .when()
                .get("/jobs/"+jobUUID).getBody().asPrettyString();
                //.then()
                //.assertThat()
                //.statusCode(is(200))
                //.and()
                //.assertThat()
                //.body("size()", equalTo(1));
        Log.info(body);

    }


}
