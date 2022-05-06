package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "results")
public class Result implements Serializable {
    @Id
    public String uuid;
    
    @NotNull
    public String ucnf;

    // non efficient implementation : duplicate articles which appear
    // in multiple results are possible, for the sake of the MVP this is 
    // done like this for now. 
    @OneToMany(fetch = FetchType.EAGER)
    private List<Article> articles;
}
