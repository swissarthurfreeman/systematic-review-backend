package ch.unige.pinfo3.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "jobs")
@ToString
public class Job implements Serializable {
    @Id
    public String uuid;

    @NotNull
    @Column(unique = true)
    public String ucnf;

    @NotNull
    public Long timestamp;

    @NotNull
    @JsonIgnore
    public int progress = 0;

    @NotNull
    public String status;

    public String getUUID() {
        return this.uuid;
    }
}
