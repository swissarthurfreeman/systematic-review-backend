package ch.unige.pinfo3.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    public String uuid;

    @NotNull
    @Column(unique=true)
    public String username;
    
    @NotNull
    @Column(unique=true)
    public String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private List<Search> searches;

    // required to be able to marshall objects since a constructor
    // was provided default empty one is not generated.
    public User() {}

    public User(String username, String email, String id) {
        this.username = username;
        this.email = email;
        this.uuid = id;
    }
}
