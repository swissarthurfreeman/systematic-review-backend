package ch.unige.pinfo3.domain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    public int totalTrials = 0;

    @JsonProperty("progress_fraction")
    @NotNull
    public double getProgressFraction () {
        return (double) progress / (double) totalTrials;
    }

    @NotNull
    public String status;

    public String getUUID() {
        return this.uuid;
    }

}
