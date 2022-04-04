package ch.unige.pinfo3.domain.model;

import ch.unige.pinfo3.domain.model.Job;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class User {
    @Id
    private final UUID uuid;
    @NotNull
    private final String username;
    @NotNull
    private final String email;

    @JsonbTransient
    private final Map<UUID, Job> jobs;

    public User(String username, String email, String uuid) {
        this.uuid = UUID.fromString(uuid);
        this.username = username;
        this.email = email;
        this.jobs = new HashMap<>();
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

    public void addJob(Job job) {
        jobs.put(job.getId(), job);
    }

    public Job getJob(UUID id) {
        return jobs.get(id);
    }

    @JsonbTransient
    public Map<UUID, Job> getJobs() {
        return jobs;
    }

}
