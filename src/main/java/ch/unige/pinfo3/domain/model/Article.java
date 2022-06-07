package ch.unige.pinfo3.domain.model;

import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="articles")
@ToString
public class Article {
    @Id public String uuid;
    @NotNull public String result_uuid;
    @Lob public String Title;
    @Lob public String Published;
    @Lob public String DOI;
    @Lob public String PmcId;
    @Lob public String Authors; 
    @Lob public String Abstract;
    @Lob public String Fulltext;
    @Lob public String Url;
    @Lob public String Journal; 
    @Lob public String labels;
    public int cluster;
    public double x;
    public double y;


    public Article(String uuid, String result_uuid, String Title, String pmcid, 
                   String Authors, String Abstract, 
                   String Full_text, String URL,
                   String Journal, String date, String labels, int cluster,
                   double x, double y) {
        this.uuid = uuid;
        this.result_uuid = result_uuid;
        this.Title = Title;
        this.PmcId = pmcid;
        this.Authors = Authors;
        this.Abstract = Abstract;
        this.Fulltext = Full_text;
        this.Url = URL;
        this.Journal = Journal;
        this.Published = date;
        this.labels = labels;
        this.cluster = cluster;
        this.x = x;
        this.y = y;
    }
    public Article() {}
}