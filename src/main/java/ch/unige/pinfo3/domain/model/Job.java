package ch.unige.pinfo3.domain.model;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Job {
    @Id
    private UUID id = UUID.randomUUID();
    @NotNull
    final public String query;
    @NotNull
    final private User user;

    public Job(String query, User user, String uuid) {
        this.query = query;
        this.user = user;
        this.id = UUID.fromString(uuid);
    }

    public UUID getId() {
        return this.id;
    }

    public String getQuery() {
        return this.query;
    }

    public User getUser() {
        return this.user;
    }
}
