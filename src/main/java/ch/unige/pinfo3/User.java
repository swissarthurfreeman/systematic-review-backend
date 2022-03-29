package ch.unige.pinfo3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    private UUID uuid;
    private String username;
    private String email;
    private Map<UUID, Job> jobs;

    public User(String username, String email){
        uuid = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.jobs = new HashMap<UUID, Job>();
    }

    public void addJob(Job job){
        jobs.put(job.getId(), job);
    }

    public Job getJob(UUID id){
        return jobs.get(id);
    }

    public Map<UUID, Job> getJobs(){
        return jobs;
    }

}
