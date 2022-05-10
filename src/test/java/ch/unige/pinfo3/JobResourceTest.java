package ch.unige.pinfo3;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

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
