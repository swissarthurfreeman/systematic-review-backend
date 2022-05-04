package ch.unige.pinfo3.domain.service;

import java.io.BufferedReader;
import org.jboss.logging.Logger;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ch.unige.pinfo3.domain.model.Job;

import java.util.stream.Collectors;

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
            
            // alternative method get array of commands and pass environment variables.
            // read script into memory
            // InputStream inputStream = getClass().getResourceAsStream("/test.sh");
            // BufferedReader shreader = new BufferedReader(new InputStreamReader(inputStream));
            // String contents = shreader.lines().collect(Collectors.joining(""));
            // String[] commands = contents.split(";");
            // String[] env = {"someEnvParameter"};
            // File path = new File(script_location);
            
            // TODO fix file not found error when using script_location
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

            // remove job from entity manager and create result
            // get job data provided from submit method
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            
            // remove by uuid
            em.remove(em.find(Job.class, dataMap.getString("job_uuid")));;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
