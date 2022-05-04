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
import java.util.UUID;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;
import io.quarkus.scheduler.Scheduled;

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
        // query external python interface here
        // add to queue
        Job job = new Job();
        String job_uuid = UUID.randomUUID().toString(); 
        job.ucnf = ucnf;
        job.uuid = job_uuid;
        job.timestamp = new Date();
        job.status = "queued";
        queue.add(job);
        
        // schedule a job => check how to execute job immidiately 
        // quartz.addJob(jobDetail, replace);
        // quartz.triggerJob(jobKey);
        // https://www.baeldung.com/quartz
        try {
            System.out.println("Starting Scheduler");
            scheduler.start();
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Building Job Detail");
        JobDetail quartz_job = JobBuilder.newJob(Task.class)
            .withIdentity(ucnf)
            .build();
        
        System.out.println("Building Trigger");
        Trigger trigger = TriggerBuilder.newTrigger()
            .startNow()
            .build();
        
        System.out.println("Adding to scheduler");
        try {
            scheduler.scheduleJob(quartz_job, trigger);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //queue.add(job);
        em.persist(job);

        // how to do asynchronous call to python interface...
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
