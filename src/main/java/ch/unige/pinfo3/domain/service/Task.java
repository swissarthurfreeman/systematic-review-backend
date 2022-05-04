package ch.unige.pinfo3.domain.service;

import java.io.BufferedReader;
import org.jboss.logging.Logger;
import java.io.InputStreamReader;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ch.unige.pinfo3.domain.model.Article;
import ch.unige.pinfo3.domain.model.Job;
import ch.unige.pinfo3.domain.model.Result;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class Task implements org.quartz.Job {
    @Inject
    Logger LOG;

    @Inject
    EntityManager em;

    @Inject
    SearchService search_service;

    // job execution Context provides the job instance with info
    // about it's runtime environment, it'll contain the ucnf query we run the clustering with
    @Transactional
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // launch sh script that sleeps 
        try {
            // we can get the .sh script location like this, python could be a property of resource...
            String script_location = this.getClass().getResource("/test.sh").toString();
            
            LOG.info("Launching Background sh Script at " + script_location);
            
            // TODO fix file not found error when using script_location
            LOG.info("script_location=" + script_location);
            String location = "/home/gordon/Documents/gordon_bsci/Sem6/PInfo/backend/build/resources/main/test.sh";
            ProcessBuilder pb = new ProcessBuilder("/bin/sh", location);
            Process proc = pb.start();

            // wait for process to finish
            proc.waitFor();


            LOG.info("Background sh Script done " + script_location);
            
            // get process output
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";

            while ((line = reader.readLine())!= null)
                output.append(line + "\n");

            LOG.info("Ouput of sh script=\n" + output);
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // remove job from entity manager and create result
        // get job data provided from submit method, if contains job_uuid (in em) and ucnf. 
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        
        // remove job by uuid since process is done
        em.remove(em.find(Job.class, dataMap.getString("job_uuid")));;

        // create corresponding result in database
        // will be replaced by result_service.getResultsFromFile(job_uuid.csv)...
    
        Result res = new Result();

        res.uuid = UUID.randomUUID().toString();
        res.ucnf = dataMap.getString("ucnf");
        
        // create bogus mock articles yielded by result
        Article article1 = new Article(
            UUID.randomUUID().toString(),
            res.uuid,
            "Quarkus Nightmare", 
            "A. Freeman, H. Haldi, C. Pendevill, L. D. Barta", 
            "Attempting to use quarkus and the nighmarish java/javascript tech stack to solve a pathological problem",
            "Get us some fucking garlic bread yes ?", 
            "http://killme.org",
            10.0,
            20.0
        );

        Article article2 = new Article(
            UUID.randomUUID().toString(),
            res.uuid,
            "Another Quarkus Nightmare", 
            "A. Freeman, H. Haldi, C. Pendevill, L. D. Barta", 
            "Attempting to use quarkus and the nighmarish java/javascript tech stack to solve a pathological problem",
            "Get us some fucking garlic bread yes ?", 
            "http://killme.org",
            20.0,
            40.0
        );

        // persist articles in db
        em.persist(article1);
        em.persist(article2);

        em.persist(res);

        //search_service.updateSearchesOf(dataMap.getString("ucnf"), res.uuid);
    }
}

