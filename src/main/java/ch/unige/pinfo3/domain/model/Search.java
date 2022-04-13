package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
}
