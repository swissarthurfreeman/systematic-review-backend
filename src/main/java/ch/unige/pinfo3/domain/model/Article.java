package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="articles")
public class Article implements Serializable {
    @Id
    public String uuid;

    @NotNull
    public String result_uuid;
    
    @NotNull
    @Lob
    public String Title;

    public String Date;

    public String PMCID;

    @Lob
    public String Authors;
    
    @Lob
    public String Abstract;
    
    @Lob    // large object
    public String Full_text;
    
    @Lob
    public String URL;

    @Lob 
    public String Journal;

    public String year;

    @Lob
    public String labels;

    @Lob
    public String text;

    public int cluster;

    @NotNull
    public double x;

    @NotNull
    public double y;

    public Article() {}

    public Article(String uuid, String result_uuid, String Title, String pmcid, 
                   String Authors, String Abstract, 
                   String Full_text, String URL,
                   String Journal, String year, String date, String labels, String text, int cluster,
                   double x, double y) {
        this.uuid = uuid;
        this.result_uuid = result_uuid;
        this.Title = Title;
        this.PMCID = pmcid;
        this.Authors = Authors;
        this.Abstract = Abstract;
        this.Full_text = Full_text;
        this.URL = URL;
        this.Journal = Journal;
        this.year = year;
        this.Date = date;
        this.labels = labels;
        this.text = text;
        this.cluster = cluster;
        this.x = x;
        this.y = y;
    }
}
