package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;


@Entity
@Table(name = "searches")
@ToString
public class Search implements Serializable {
    @Id
    public String uuid;
    
    // this is the subject claim from jwt
    public String user_uuid;
    
    @NotNull
    @NotBlank(message = "Query cannot be blank you madman")
    @Size(min = 15, message = "Query is too short !")
    public String query;
    public String ucnf;

    public Date timestamp;

    @JsonIgnore
    public String job_uuid;

    // maybe to refactor to optional.
    // many searches can refer to one job.
    @JsonProperty("job_uuid")
    @ManyToOne(targetEntity = Job.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "JOB_UUID")
    public String getJobUUID() {
        return this.job_uuid;
    }

    public void setJobUUID(String uuid) {
        this.job_uuid = uuid;
    }

    private String result_uuid;
    
    @JsonProperty("result_uuid")
    @ManyToOne(targetEntity = Result.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "RESULT_UUID")
    public String getResultUUID() {
        return this.result_uuid;
    }

    public void setResultUUID(String uuid) {
        this.result_uuid= uuid;
    }
}
