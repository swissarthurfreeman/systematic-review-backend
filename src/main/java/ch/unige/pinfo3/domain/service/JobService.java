package ch.unige.pinfo3.domain.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ch.unige.pinfo3.domain.model.Job;
import groovy.lang.Singleton;

@ApplicationScoped
@Singleton              // there can only be one jobber.
public class JobService {
    @Inject 
    EntityManager em;
    
    public String submit(String ucnf) {
        // query external python interface here
        // add to queue
        Job job = new Job();
        String job_uuid = UUID.randomUUID().toString(); 
        job.uuid = job_uuid;
        job.timestamp = new Date();
        job.status = "queued";

        em.persist(job);

        // asynchronous call to python interface...
        // ...
        // @Inject org.eclipse.microprofile.context.ManagedExecutor
        // https://stackoverflow.com/questions/67444442/right-way-to-start-a-background-task-per-request-in-quarkus
        
        return job_uuid;
    }

    public String getStatus(String uuid) {
        return "All good captain !";
    }
}
