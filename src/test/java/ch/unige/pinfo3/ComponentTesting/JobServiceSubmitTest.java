package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import ch.unige.pinfo3.domain.service.SearchService;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;

@QuarkusTest
public class JobServiceSubmitTest {

    @Inject
    EntityManager em;

    @Inject
    JobService js;

    @InjectMock
    SearchService ss;

    @Test
    void submitTest(){

        String commitJobUUID = js.submit("ebola AND monkeypox AND covid");

        Log.info(commitJobUUID);

        Optional<Job> job = js.getJob(commitJobUUID);

        Log.info(job.toString());

        Assertions.assertTrue(job.isPresent());

        Assertions.assertEquals(job.get().uuid, commitJobUUID);

    }


}
