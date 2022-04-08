package ch.unige.pinfo3.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@EqualsAndHashCode
public class Job {
    @Getter
    private UUID id;

    @Getter
    @Setter
    public String query;

    @Getter
    @Setter
    private User user;

    public Job() {
        this.id = UUID.randomUUID();
    }

    public Job(String query, User user) {
        this();
        this.query = query;
        this.user = user;
    }
}
