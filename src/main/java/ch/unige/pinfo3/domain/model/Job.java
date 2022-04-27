package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import java.util.Date;
// import java.util.Optional;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "jobs")
public class Job implements Serializable {
    @Id
    public String uuid;

    @NotNull
    public Date timestamp;

    @NotNull
    public String status;

    public String getUUID() {
        return this.uuid;
    }
}
