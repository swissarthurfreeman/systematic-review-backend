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

    public String Title;
    public String Date; 
    public String DOI;
    public String PMCID;
    public String Authors;
    public String Abstract;
    public String Full_text;
    public String URL;
    public String Journal;
    public String Year;
    public String labels;
    public String text;
    public String cluster;
    public double x;
    public double y;
}
