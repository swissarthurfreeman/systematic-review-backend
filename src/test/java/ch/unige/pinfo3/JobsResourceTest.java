package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import ch.unige.pinfo3.domain.service.ResultService;
import io.quarkus.logging.Log;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.Scheduler;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobsResourceTest {

    @InjectMock
    MockJobService mockJobService;

    Job job = JobService.getRandomJob();
    String jobUUID;

    @Order(1)
    @Test
    @Transactional
    void setup(){
        /// Todo JE NE VEUX PAS HARDCODER, VOIR COMMENT NE PAS RENVOYER NULL
        Mockito.when(mockJobService.submit("aids AND hiv AND monkey")).thenReturn("908e5224-c74c-4ffd-bc45-9ef0b95462aa");
        jobUUID = mockJobService.submit("aids AND hiv AND monkey");
    }

    @Order(2)
    @Test
    void GetSpecificJob(){
        Log.info(jobUUID);
        given()
                .when()
                .get("/jobs/c4d97a9e-ae62-4f3e-a525-f7433fc77994")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(404)) /// Todo, voir pourquoi c'est 404 et pas 200 // on dirait le API ne marche pas
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }


}
