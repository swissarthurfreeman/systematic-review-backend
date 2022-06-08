package ch.unige.pinfo3.ComponentTesting;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.domain.service.JobService;
import ch.unige.pinfo3.domain.service.SearchService;
import ch.unige.pinfo3.utils.RandomProducer;
import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@QuarkusTestResource(OidcWiremockTestResource.class)
public class JobServiceTest {

    @InjectMock
    EntityManager em;

    @Inject
    JobService js;

    @InjectMock
    SearchService ss;

    @Test
    void getJobsOfUserTest(){
        String userUUID = UUID.randomUUID().toString();
        Job job = RandomProducer.getRandomJob();
        String jobUUID = job.uuid;
        List<Search> searches = new ArrayList<Search>();
        searches.add(RandomProducer.getRandomSearch(userUUID, null, UUID.randomUUID().toString()));
        searches.add(RandomProducer.getRandomSearch(userUUID, null, UUID.randomUUID().toString()));
        searches.add(RandomProducer.getRandomSearch(userUUID, jobUUID, null));
        List<Job> jobs = new ArrayList<>();

        Mockito.when(em.find(Job.class, jobUUID)).thenReturn(job);
        Mockito.when(ss.getSearchesOf(userUUID)).thenReturn(searches);

        for(Search s: searches){
            if(s.job_uuid != null){
                jobs.add(em.find(Job.class, s.job_uuid));
            }
        }

        List<Job> jobsTest = new ArrayList<>();
        jobsTest.add(job);

        Assertions.assertEquals(js.getJobsOfUser(userUUID), jobsTest);

    }

    @Test
    void getJobExistingTest(){
        // tester getJob quand un utilisateur a un job
        Job job = new Job();
        Mockito.when(em.find(Job.class, job.uuid)).thenReturn(job);

        List<Job> jobs = new ArrayList<Job>();
        jobs.add(job);

        var jobRes = js.getJob(job.uuid);

        Assertions.assertEquals(jobRes, Optional.of(job));

    }

}
