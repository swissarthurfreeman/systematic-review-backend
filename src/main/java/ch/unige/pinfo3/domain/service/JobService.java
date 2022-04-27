package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.hibernate.engine.spi.Managed;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import io.quarkus.scheduler.Scheduled.SkipPredicate;

// there can only be one jobber.
@ApplicationScoped
public class JobService {
    @Inject 
    EntityManager em;

    @Inject
    ManagedExecutor executor;

    static final ArrayList<Job> queue = new ArrayList<Job>();

    @Transactional
    public String submit(String ucnf) {
        // query external python interface here
        // add to queue
        Job job = new Job();
        String job_uuid = UUID.randomUUID().toString(); 
        job.ucnf = ucnf;
        job.uuid = job_uuid;
        job.timestamp = new Date();
        job.status = "queued";

        queue.add(job);
        em.persist(job);

        // asynchronous call to python interface...
        // ... fucking nightmare
        // @Inject org.eclipse.microprofile.context.ManagedExecutor
        // https://stackoverflow.com/questions/67444442/right-way-to-start-a-background-task-per-request-in-quarkus
        return job_uuid;
    }

    // in order to mock functioning we remove a job after 10 seconds...
    @Scheduled(every="10s")
    @Transactional
    void convertJobToResult() {
        if(queue.size() > 0) {
            Job job = queue.remove(0);
            Job toRemove = em.find(Job.class, job.uuid);
            em.remove(toRemove);
            Result newResult = new Result();
            newResult.uuid = job.uuid;
            newResult.Abstract = "Blabla about the blablas";
            newResult.Authors = "Gordon Freeman";
            newResult.Title = "Gordoing in the time of 10 Artours";
            em.persist(newResult);
        }
    }

    @Transactional
    public List<Job> getAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
		criteria.from(Job.class);
		return em.createQuery(criteria).getResultList();
    }

    public String getStatus(String uuid) {
        return "All good captain !";
    }
}
