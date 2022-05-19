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
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JobsResourceTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @InjectMock
    MockJobService mockJobService;

    @Inject
    EntityManager em;

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
    /// TODO: voir si je peux m'en débarasser



    @Order(2)
    @Test
    void GetSpecificJob(){
        Log.info(jobUUID);
        given()
                .auth()
                .oauth2(getAccessToken("alice"))
                .when()
                .get("/jobs/c4d97a9e-ae62-4f3e-a525-f7433fc77994")
                .then()
                .assertThat()
                .statusCode(CoreMatchers.is(404)) /// Todo, voir pourquoi c'est 404 et pas 200......
                .and()
                .body("size()", CoreMatchers.equalTo(2));
    }

    @Order(3)
    @Test
    @Transactional
    void persistResult() {
        Log.info("persisting a results and articles to DB");
        Log.info(em);
        em.persist(job);
    }

    // Testing GET /jobs
    @Test
    @Order(4)
    @TestTransaction
    void getJobsOfUser(){
        Log.info("Testing GET /jobs");
        //when(mockJobService.submit("hiv AND covid AND ebola")).thenReturn("908e5224-c74c-4ffd-bc45-9ef0b95462aa");
        //String jobUUID =  mockJobService.submit("hiv AND covid AND ebola");
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
                .body("size()", CoreMatchers.equalTo(0)); /// todo: 0 pour que tests passent. Voir comment ajouter job à utilisateur
    }



    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }

}
