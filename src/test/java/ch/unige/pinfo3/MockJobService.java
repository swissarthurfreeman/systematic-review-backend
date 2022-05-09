package ch.unige.pinfo3;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.service.JobService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Mock
@ApplicationScoped
public class MockJobService extends JobService {

    @Inject
    EntityManager em;

    @Override
    public String submit(String ucnf){
        Job job = getRandomJob();
        em.persist(job);

        return job.uuid;
    }
}
