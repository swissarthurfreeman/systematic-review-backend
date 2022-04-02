package ch.unige.pinfo3;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbTransient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RegisterForReflection
public class User {
    private final UUID uuid;
    private final String username;
    private final String email;

    @JsonbTransient
    private final Map<UUID, Job> jobs;

    public User(String username, String email){
        uuid = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.jobs = new HashMap<UUID, Job>();
    }

    public UUID getId() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void addJob(Job job){
        jobs.put(job.getId(), job);
    }

    public Job getJob(UUID id){
        return jobs.get(id);
    }

    @JsonbTransient
    public Map<UUID, Job> getJobs(){
        return jobs;
    }

}
