package ch.unige.pinfo3;

import java.util.UUID;

public class Job {

    final public String query;
    final private User user;

    final public UUID id = UUID.randomUUID();

    Job(String query, User user) {
        this.query = query;
        this.user = user;
    }

    UUID getId() { return this.id; }
}
