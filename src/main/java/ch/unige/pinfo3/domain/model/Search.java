package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "searches")
public class Search implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uuid;             // we don't generate UUIDs ourselves, DB does this.
    
    @NotNull
    private long user_uuid;
    
    @NotNull
    private String query;
    
    @NotNull
    private String ucnf;
}
