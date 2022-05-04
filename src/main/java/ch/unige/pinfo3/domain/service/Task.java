package ch.unige.pinfo3.domain.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Task implements org.quartz.Job {

    // job execution Context provides the job instance with info
    // about it's runtime environment
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // TODO Auto-generated method stub
        // launch sh script that sleeps 
        System.out.println("\n\n\n\n\n\nLOOOOOOLLLL lolololol\n\n\n\n\n\n\n");
        
    }
}
