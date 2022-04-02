package ch.unige.pinfo3;

import java.util.UUID;

public class Job {
    // add public getter to return value in json
    final public String query;
    final private User user;

    private UUID id = UUID.randomUUID();

    Job(String query, User user, String uuid) {
        this.query = query;
        this.user = user;
        this.id = UUID.fromString(uuid);
    }

    public UUID getId() { return this.id; }
    public String getQuery() { return this.query; }
    public User getUser() { return this.user; }
}
