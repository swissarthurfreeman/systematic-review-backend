package ch.unige.pinfo3.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class User {
    @Getter
    private UUID id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @Getter
    private Map<UUID, Job> jobs;

    private static volatile User dummy = null;

    public static User getDummy() {
        if (dummy == null) {
            synchronized (Singleton.class) {
                if (dummy == null) {
                    dummy = new User("john.doe", "john.doe@example.org");
                    dummy.id = UUID.fromString("0ce40162-aaaa-5666-aaaa-8f36f394ffd9");
                }
            }
        }
        return dummy;
    }

    public User() {
        this.id = UUID.randomUUID();
        this.jobs = new HashMap<>();
    }

    public User(String username, String email) {
        this();
        this.username = username;
        this.email = email;
    }

    public void addJob(Job job) {
        jobs.put(job.getId(), job);
    }

    public Job getJob(UUID id) {
        return jobs.get(id);
    }

}
