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
import io.quarkus.logging.Log;

import io.smallrye.reactive.messaging.kafka.Record;

// there can only be one jobber.
@ApplicationScoped
public class JobService {
    @Inject
    EntityManager em;

    @Inject
    SearchService searchService;

    static final ArrayList<Job> queue = new ArrayList<Job>();


    @Inject
    @Channel("ucnfs")
    Emitter<String> ucnfEmitter;
    
    @Transactional
    public String submit(String ucnf) {
        
        Job commit_job = new Job();
        commit_job.uuid = UUID.randomUUID().toString();
        commit_job.ucnf = ucnf;
        commit_job.status = "queued";
        commit_job.timestamp = new Date().getTime();
        em.persist(commit_job);

        ucnfEmitter.send(ucnf);
        return commit_job.uuid;
    }
     
    public List<Job> getJobsOfUser(String user_uuid) {
        List<Search> searches = searchService.getSearchesOf(user_uuid);
        List<Job> jobs = new ArrayList<>();
        for(Search s: searches) {
            if(s.job_uuid != null)
                jobs.add(em.find(Job.class, s.job_uuid));
        }
        return jobs;   
    }
    
    public Optional<Job> getJob(String job_uuid) {
        return Optional.ofNullable(em.find(Job.class, job_uuid));
    }

    public static Job getRandomJob(){
        String[] status = {"queued"};
        Job job = new Job();
        Faker fk = new Faker();
        job.uuid = UUID.randomUUID().toString();
        job.ucnf = fk.expression("#{lorem.word} AND #{lorem.word} AND #{lorem.word}");
        job.timestamp = new Date().getTime();
        job.status = status[(int) (Math.random() * status.length)];
        return job;
    }
}
