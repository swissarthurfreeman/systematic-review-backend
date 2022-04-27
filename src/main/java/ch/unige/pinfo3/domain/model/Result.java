package ch.unige.pinfo3.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "results")
public class Result implements Serializable {
    @Id
    public String uuid;

    @NotNull
    public String data;
}
