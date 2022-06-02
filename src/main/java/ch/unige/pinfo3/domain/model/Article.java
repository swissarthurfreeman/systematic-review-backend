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
    public String title;

    public String date;

    public String pmcid;

    @Lob
    public String authors;
    
    @Lob
    public String Abstract;
    
    @Lob    // large object
    public String full_text;
    
    @Lob
    public String url;

    @Lob 
    public String journal;

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
        this.title = Title;
        this.pmcid = pmcid;
        this.authors = Authors;
        this.Abstract = Abstract;
        this.full_text = Full_text;
        this.url = URL;
        this.journal = Journal;
        this.year = year;
        this.date = date;
        this.labels = labels;
        this.text = text;
        this.cluster = cluster;
        this.x = x;
        this.y = y;
    }
}
