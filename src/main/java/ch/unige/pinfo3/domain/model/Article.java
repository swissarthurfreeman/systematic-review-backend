package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    public String Title;
    
    @NotNull
    public String Authors;
    
    @NotNull
    public String Abstract;
    
    @NotNull
    public String Full_text;
    
    @NotNull
    public String URL;

    @NotNull
    public String university;

    @NotNull
    public double x;
    
    @NotNull
    public double y;

    public Article() {}

    public Article(String uuid, String result_uuid, String Title, 
                   String Authors, String Abstract, 
                   String Full_text, String URL,
                   String university,
                   double x, double y) {
        this.uuid = uuid;
        this.result_uuid = result_uuid;
        this.Title = Title;
        this.Authors = Authors;
        this.Abstract = Abstract;
        this.Full_text = Full_text;
        this.URL = URL;
        this.university = university;
        this.x = x;
        this.y = y;
    }
}
