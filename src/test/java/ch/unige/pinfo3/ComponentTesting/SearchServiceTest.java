package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.service.SearchService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.junit.jupiter.api.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

import static ch.unige.pinfo3.domain.service.JobService.getRandomJob;
import static ch.unige.pinfo3.domain.service.ResultService.getRandomResult;
import static ch.unige.pinfo3.domain.service.SearchService.getRandomSearch;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTestResource(OidcWiremockTestResource.class)
public class SearchServiceTest {

    @Inject
    EntityManager em;

    @Inject
    SearchService ss;

    @Test
    @ApplicationScoped
    @Transactional
    void createResultsNotEmpty(){
        // testing SearchService.java create if(!results.isEmpty())

        Search search = getRandomSearch(UUID.randomUUID().toString(), null, UUID.randomUUID().toString());

        Result result = getRandomResult();
        result.uuid = search.getResultUUID();
        result.ucnf = search.ucnf;

        em.persist(result);

        Search resultTest = ss.create(search);

        Assertions.assertNull(resultTest.getJobUUID());
        Assertions.assertEquals(resultTest.getResultUUID(), result.uuid);

    }

    @Test
    @ApplicationScoped
    @Transactional
    void createJobsNotEmpty(){
        // testing SearchService.java create if(!jobs.isEmpty())

        Search search = getRandomSearch(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);

        Job job = getRandomJob();
        job.uuid = search.getJobUUID();
        job.ucnf = search.ucnf;

        em.persist(job);

        Search resultTest = ss.create(search);

        Assertions.assertNull(resultTest.getResultUUID());
        Assertions.assertEquals(resultTest.getJobUUID(), job.uuid);

    }

}
