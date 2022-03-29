package ch.unige.pinfo3;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private UUID uuid;
    private String username;
    private String email;
    private ArrayList<Job> jobs;

    public User(String username, String email){
         uuid = UUID.randomUUID();
         this.username = username;
         this.email = email;
         this.jobs = new ArrayList<Job>();
    }

    public void addJob(Job job){
        jobs.add(job);
    }


}
