package ch.unige.pinfo3.domain.service;

import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import io.quarkus.logging.Log;

/**
 * This class is what gets fed to the quartz scheduler. 
 * The quartz scheduler will maintain a thread pool and choose and dispatch
 * execution of this method to one of them asynchronously. 
 * It must contain a method called execute(context) where 
 * context can be expanded by passing in parameters before scheduling. 
 * 
 * The goal for MVP of this class will be to : 
 * - Execute Python program by running a process command. 
 * - Wait for execution to finish
 * - Read all results from disk via .csv
 * - create the appropriate Result and Article instances, persist them
 *   in database.
 * - remove job from database (was created before scheduling task).  
 */
@ApplicationScoped
public class Task implements org.quartz.Job {

    @Inject
    EntityManager em;

    @Inject
    SearchService searchService;

    @Inject
    ArticleService articleService;

    private Process proc;

    private final SecureRandom random = new SecureRandom();

    // job execution Context provides the job instance with info
    // about it's runtime environment, it'll contain the ucnf query we run the clustering with
    @Transactional
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // launch sh script that sleeps 
    
        executeProcess();
            
        // get/stream process standard output if needed, we can write updates to db if required. 
        // call getProcessOutput here if needed.
        // get job data provided from submit method, if contains job_uuid (in em) and ucnf.
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        

        // remove job from entity manager job by uuid since process is done
        em.remove(em.find(Job.class, dataMap.getString("job_uuid")));

        // create corresponding result in database
        // will be replaced by result_service.getResultsFromFile(job_uuid.csv)...
        Result res = new Result();

        res.uuid = UUID.randomUUID().toString();
        res.ucnf = dataMap.getString("ucnf");
        
        // create bogus mock articles yielded by result these will have to be read from disk.        
        // persist articles in db
        for(int i=0; i < this.random.nextInt(5) + 5; i++)
            em.persist(ArticleService.getRandomArticle(res.uuid));
        
        // persist result
        em.persist(res);

        searchService.updateSearchesOf(dataMap.getString("ucnf"), res.uuid);
    }

    public void executeProcess() {
        // we can get the .sh script location like this, python could be a property of resource...
        // beware URL.toString() contains path prefixed by protocol. 
        String scriptLocation = this.getClass().getResource("/test.sh").getPath();

        Log.info("Launching Background sh Script at " + scriptLocation);
        
        Log.info("scriptLocation=" + scriptLocation);
        ProcessBuilder pb = new ProcessBuilder("/bin/sh", scriptLocation);
        
        // start and wait for process to finish
        try {
            this.proc = pb.start();
            this.proc.waitFor();
            Log.info("Background Script done ");
        } catch (Exception e) {
            Log.error(e.getMessage());
        } 
    }

    public String getProcessOutput() {
        StringBuffer output = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.proc.getInputStream()));
        String line = "";

        try {
            while ((line = reader.readLine())!= null)
                output.append(line + "\n");
        } catch (IOException e) {
            Log.error(e.getMessage());
        }

        Log.info("Ouput of sh script=\n" + output.toString());
        return output.toString();
    }
}

