package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.service.SearchService;
import ch.unige.pinfo3.utils.RandomProducer;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.junit.jupiter.api.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    Search search1;
    Search search2;

    @Test
    @ApplicationScoped
    @Transactional
    @Order(1)
    void createResultsNotEmpty() {
        // testing SearchService.java create if(!results.isEmpty())

        search1 = RandomProducer.getRandomSearch(UUID.randomUUID().toString(), null, UUID.randomUUID().toString());

        Result result = RandomProducer.getRandomResult();
        result.uuid = search1.getResultUUID();
        result.ucnf = search1.ucnf;

        em.persist(result);

        Search resultTest = ss.create(search1);

        Assertions.assertNull(resultTest.getJobUUID());
        Assertions.assertEquals(resultTest.getResultUUID(), result.uuid);

    }

    @Test
    @ApplicationScoped
    @Transactional
    @Order(2)
    void createJobsNotEmpty() {
        // testing SearchService.java create if(!jobs.isEmpty())

        search2 = RandomProducer.getRandomSearch(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null);

        Job job = RandomProducer.getRandomJob();
        job.uuid = search2.getJobUUID();
        job.ucnf = search2.ucnf;

        em.persist(job);

        Search resultTest = ss.create(search2);

        Assertions.assertNull(resultTest.getResultUUID());
        Assertions.assertEquals(resultTest.getJobUUID(), job.uuid);

    }

    @Test
    @Order(3)
    void getAllTest() {
        // ici je teste avec les deux search qui ont deja ete persiste
        List<Search> searches = new ArrayList<>();
        searches.add(search1);
        searches.add(search2);

        Log.info(search1.timestamp);
        Log.info(ss.getAll().get(0).timestamp);

        Assertions.assertEquals(searches.toString(), ss.getAll().toString());

    }

    @Test
    @Order(4)
    void updateSearchesOfTest() {
        Result result = RandomProducer.getRandomResult();
        result.ucnf = search2.ucnf;
        ss.updateSearchesOf(result.ucnf, result.uuid);

        search2.setJobUUID(null);
        search2.setResultUUID(result.uuid);

        Assertions.assertEquals(ss.getAll().get(1).toString(), search2.toString());
    }
}
