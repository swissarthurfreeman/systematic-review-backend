package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.utils.QueryUtils;

import org.jboss.logging.Logger;
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

    @Inject
    Logger logger;

    @Inject
    QueryUtils qu;

    static final ArrayList<Job> queue = new ArrayList<Job>();

    @Transactional
    public String submit(String ucnf) {        
        // check database if job with the provided ucnf exists
        List<Job> result = qu.select(Job.class, "ucnf", ucnf, em);

        if(result.size() == 1) { 
            logger.info("Job with that UCNF already exists, returning job_uuid...");
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
            logger.error(e.getMessage());
        }

        return commit_job.getUUID();
    }

    @Transactional
    public List<Job> getAll() {
        return qu.getAll(Job.class, em);
    }

    public String getStatus(String uuid) {
        return "All good captain !";
    }
}
