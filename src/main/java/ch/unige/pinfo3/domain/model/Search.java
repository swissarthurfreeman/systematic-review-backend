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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "searches")
public class Search implements Serializable {
    @Id
    public String uuid;
    
    @NotNull
    public String user_uuid;
    
    @NotNull
    public String query;
    
    @NotNull
    public String ucnf;

    @NotNull
    public Date timestamp;

    private String job_uuid;

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
        this.result_uuid = null; // ajouter???
    }

    private String result_uuid;
    
    @JsonProperty("result_uuid")
    @ManyToOne(targetEntity = Result.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "RESULT_UUID")
    public String getResultUUID() {
        return this.result_uuid;
    }

    public void setResultUUID(String uuid) {
        this.result_uuid = uuid;
        this.job_uuid = null; 
    }
}
