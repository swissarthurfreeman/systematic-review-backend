package ch.unige.pinfo3;

import javax.json.bind.annotation.JsonbProperty;
import java.util.UUID;

public class Job {
    @JsonbProperty("query")
    final public String query;
    @JsonbProperty("user")
    final private User user;

    private UUID id = UUID.randomUUID();

    Job(String query, User user, String uuid) {
        this.query = query;
        this.user = user;
        this.id = UUID.fromString(uuid);
    }

    UUID getId() { return this.id; }
    String getQuery() { return this.query; }
    User getUser() { return this.user; }
}
