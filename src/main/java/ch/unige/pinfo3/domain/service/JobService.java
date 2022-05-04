package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ch.unige.pinfo3.domain.model.Job;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

// there can only be one jobber.
@ApplicationScoped
public class JobService {
    @Inject 
    EntityManager em;

    @Inject
    Scheduler scheduler;

    static final ArrayList<Job> queue = new ArrayList<Job>();

    @Transactional
    public String submit(String ucnf) {
        
        // check database if job with the provided ucnf exists
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
        Root<Job> root = criteria.from(Job.class);
        criteria.select(root).where(builder.equal(root.get("ucnf"), ucnf));
        List<Job> result = em.createQuery(criteria).getResultList();

        if(result.size() == 1) { 
            System.out.println("Job with that UCNF already exists !");
            return result.get(0).uuid;
        }
        
        // TODO check database if a result with that ucnf already exists

        // persist job in database
        Job commit_job = new Job();
        commit_job.uuid = UUID.randomUUID().toString();
        commit_job.ucnf = ucnf;
        commit_job.status = "queued";
        commit_job.timestamp = new Date();
        em.persist(commit_job);

        JobDetail job_info = JobBuilder.newJob(Task.class)
            .withIdentity(ucnf)
            .usingJobData("job_uuid", commit_job.uuid)
            .usingJobData("ucnf", commit_job.ucnf)
            .build();
        
        Trigger trigger = TriggerBuilder.newTrigger()
            .startNow()
            .build();

        try {
            scheduler.scheduleJob(job_info, trigger);
        } catch(SchedulerException e) {
            e.printStackTrace();
        }

        return commit_job.getUUID();
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
