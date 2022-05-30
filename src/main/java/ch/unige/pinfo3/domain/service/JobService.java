package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import ch.unige.pinfo3.domain.model.Search;
import ch.unige.pinfo3.utils.QueryUtils;

import com.github.javafaker.Faker;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import io.quarkus.logging.Log;

// there can only be one jobber.
@ApplicationScoped
public class JobService {
    @Inject
    EntityManager em;

    @Inject
    Scheduler scheduler;

    @Inject
    SearchService searchService;

    static final ArrayList<Job> queue = new ArrayList<Job>();


    @Inject
    @Channel("jobs")
    Emitter<String> ucnfEmitter;
    
    @Transactional
    public String submit(String ucnf) {
        
        /*Job commit_job = new Job();
        commit_job.uuid = UUID.randomUUID().toString();
        commit_job.ucnf = ucnf;
        commit_job.status = "queued";
        commit_job.timestamp = new Date();
        em.persist(commit_job);*/

        ucnfEmitter.send(ucnf);
        return "AAAA";
        //return commit_job.getUUID();

        // check database if job with the provided ucnf exists
        /*List<Result> results = QueryUtils.select(Result.class, "ucnf", ucnf, em);
        
        if(!results.isEmpty()) { 
            Log.info("Result with that UCNF already exists, returning result_uuid...");
            return results.get(0).uuid;
        }
        
        List<Job> jobs = QueryUtils.select(Job.class, "ucnf", ucnf, em);
        if(!jobs.isEmpty()) { 
            Log.info("Job with that UCNF already exists, returning job_uuid...");
            return jobs.get(0).uuid;
        }

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
            Log.error(e.getMessage());
        }

        return commit_job.getUUID(); */
    }
     
    @Transactional
    public List<Job> getJobsOfUser(String user_uuid) {
        List<Search> searches = searchService.getSearchesOf(user_uuid);
        List<Job> jobs = new ArrayList<>();
        for(Search s: searches) {
            if(s.job_uuid != null)
                jobs.add(em.find(Job.class, s.job_uuid));
        }
        return jobs;   
    }
    
    @Transactional
    public Optional<Job> getJob(String job_uuid) {
        return Optional.ofNullable(em.find(Job.class, job_uuid));
    }

    public static Job getRandomJob(){
        String[] status = {"queued"};
        Job job = new Job();
        Faker fk = new Faker();
        job.uuid = UUID.randomUUID().toString();
        job.ucnf = fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        job.timestamp = new Date();
        job.status = status[(int) (Math.random() * status.length)];
        return job;
    }
}
