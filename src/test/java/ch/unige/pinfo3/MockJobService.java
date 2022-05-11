package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.logging.Log;
import io.quarkus.test.Mock;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.BufferedWriter;
import java.io.FileWriter;

@Mock
@ApplicationScoped
public class MockJobService extends JobService {

    @Inject
    EntityManager em;

    @Override
    public String submit(String ucnf) {
        Job job = getRandomJob();
        job.uuid = "908e5224-c74c-4ffd-bc45-9ef0b95462aa";
        job.ucnf = ucnf;
        em.persist(job);

        Log.error(job.uuid);

        return job.uuid;
    }
}
