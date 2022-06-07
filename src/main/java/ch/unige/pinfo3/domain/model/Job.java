package ch.unige.pinfo3.domain.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "jobs")
public class Job implements Serializable {
    @Id
    public String uuid;

    @NotNull
    @Column(unique=true) 
    public String ucnf;

    @NotNull
    public Long timestamp;

    @NotNull
    public String status;

    public String getUUID() {
        return this.uuid;
    }
}
